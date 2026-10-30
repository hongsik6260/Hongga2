package com.hongja.carlauncher;
import android.app.*;
import android.os.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.view.*;
import android.widget.*;
import java.text.*;
import java.util.*;
public class MainActivity extends Activity {
 int bg=0xff090f1e, panel=0xff14223a, accent=0xff00b4ff;
 LinearLayout root, side; TextView clock;
 android.content.SharedPreferences pref;
 String nav="com.skt.tmap.ku",music="";
 TextView text(String s,int size){ TextView t=new TextView(this);t.setText(s);t.setTextColor(-1);t.setTextSize(size);t.setPadding(16,10,16,10);t.setGravity(Gravity.CENTER);return t; }
 GradientDrawable box(int color){GradientDrawable d=new GradientDrawable();d.setColor(color);d.setCornerRadius(22);d.setStroke(2,accent);return d;}
 void save(){pref.edit().putInt("color",accent).putString("nav",nav).putString("music",music).apply();}
 @Override public void onCreate(Bundle b){super.onCreate(b);pref=getSharedPreferences("car",0);accent=pref.getInt("color",accent);nav=pref.getString("nav",nav);music=pref.getString("music","");getWindow().getDecorView().setSystemUiVisibility(5894);draw();}
 void draw(){
 root=new LinearLayout(this);root.setOrientation(1);root.setPadding(16,10,16,10);root.setBackgroundColor(bg);setContentView(root);
 TextView title=text("◈  HONGJA CAR  |  v0.1",20);title.setTextColor(accent);root.addView(title,new LinearLayout.LayoutParams(-1,52));
 LinearLayout body=new LinearLayout(this);root.addView(body,new LinearLayout.LayoutParams(-1,0,1));
 LinearLayout map=new LinearLayout(this);map.setOrientation(1);map.setPadding(12,12,12,12);map.setBackground(box(panel));
 LinearLayout.LayoutParams mp=new LinearLayout.LayoutParams(0,-1,7);mp.setMargins(0,0,10,0);body.addView(map,mp);
 TextView navTitle=text("NAVIGATION",18);navTitle.setTextColor(accent);map.addView(navTitle);
 TextView hint=text("티맵 / 카카오내비\n\n내비게이션 실행 버튼을 눌러주세요.\n\n※ 지도 70:30 실제 분할은 후속 버전에서 지원",19);
 map.addView(hint,new LinearLayout.LayoutParams(-1,0,1));
 LinearLayout row=new LinearLayout(this);map.addView(row);
 add(row,"▶ 내비 실행",()->launch(nav));add(row,"내비 선택",()->chooseNav());
 side=new LinearLayout(this);side.setOrientation(1);side.setPadding(10,10,10,10);side.setBackground(box(panel));body.addView(side,new LinearLayout.LayoutParams(0,-1,3));
 clock=text("",30);clock.setTextColor(accent);side.addView(clock,new LinearLayout.LayoutParams(-1,0,1));
 add(side,"♫ 음악 실행",()->{if(music.isEmpty())chooseMusic();else launch(music);});
 add(side,"음악 앱 선택",()->chooseMusic());add(side,"⚙ 설정",()->settings());
 new Handler().post(new Runnable(){public void run(){if(clock!=null && clock.isAttachedToWindow()){clock.setText(new SimpleDateFormat("HH:mm",Locale.KOREA).format(new Date()));new Handler().postDelayed(this,1000);}}});
 LinearLayout foot=new LinearLayout(this);root.addView(foot);
 add(foot,"HOME",()->draw());add(foot,"NAV",()->launch(nav));add(foot,"MUSIC",()->{if(music.isEmpty())chooseMusic();else launch(music);});add(foot,"SETTINGS",()->settings());
 }
 void add(LinearLayout row,String s,Runnable action){TextView t=text(s,15);t.setBackground(box(panel));LinearLayout.LayoutParams p;
 if(row.getOrientation()==LinearLayout.HORIZONTAL){p=new LinearLayout.LayoutParams(0,55,1);p.setMargins(3,3,3,3);}else{p=new LinearLayout.LayoutParams(-1,0,1);p.setMargins(3,3,3,3);}
 row.addView(t,p);t.setOnClickListener(v->action.run());}
 void launch(String p){Intent i=getPackageManager().getLaunchIntentForPackage(p);if(i==null){new AlertDialog.Builder(this).setMessage("앱이 설치되어 있지 않거나 실행할 수 없습니다.").setPositiveButton("확인",null).show();return;}try{startActivity(i);}catch(Exception e){Toast.makeText(this,"실행 오류",0).show();}}
 void chooseNav(){String[] n={"티맵","카카오내비"};new AlertDialog.Builder(this).setTitle("내비 선택").setItems(n,(d,w)->{nav=w==0?"com.skt.tmap.ku":"com.locnall.KimGiSa";save();}).show();}
 void chooseMusic(){
 Intent q=new Intent(Intent.ACTION_MAIN);q.addCategory(Intent.CATEGORY_LAUNCHER);
 PackageManager pm=getPackageManager();List<ResolveInfo> all=pm.queryIntentActivities(q,0);
 ArrayList<String> names=new ArrayList<>(), pkgs=new ArrayList<>();
 for(ResolveInfo ri:all){String p=ri.activityInfo.packageName;if(p.equals(getPackageName())||pkgs.contains(p))continue;pkgs.add(p);names.add(ri.loadLabel(pm).toString());}
 String[] arr=names.toArray(new String[0]);new AlertDialog.Builder(this).setTitle("실행할 음악 앱 선택").setItems(arr,(d,w)->{music=pkgs.get(w);save();Toast.makeText(this,"선택: "+arr[w],0).show();}).setNegativeButton("취소",null).show();
 }
 void settings(){String[] n={"테마 색상","내비게이션 선택","음악 앱 선택","시동 ON 자동 실행 (후속 버전)","시동 OFF 화면 끄기 (후속 버전)"};new AlertDialog.Builder(this).setTitle("홍자 CAR 설정").setItems(n,(d,w)->{switch(w){case 0:colors();break;case 1:chooseNav();break;case 2:chooseMusic();break;default:Toast.makeText(this,"이 기능은 다음 버전에서 구현합니다.",1).show();}}).show();}
 void colors(){String[] n={"블루","레드","퍼플","그린","골드","화이트"};int[] c={0xff00b4ff,0xffff4860,0xffb27aff,0xff39e6a0,0xffffcb56,0xffffffff};new AlertDialog.Builder(this).setTitle("LED 색상").setItems(n,(d,w)->{accent=c[w];save();draw();}).show();}
}
