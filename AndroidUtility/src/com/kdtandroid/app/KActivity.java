package com.kdtandroid.app;

import java.util.HashMap;
import java.util.Timer;

import com.kdt.util.schedule.Scheduler;
import com.kdtandroid.util.AndroidUtility;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

abstract public class KActivity extends Activity implements UIinterface{
	public enum REQUEST {
		THEME			 			("THEME")
		;
		private final String value;   
		REQUEST(String value) {
			this.value = value;
		}
		public String getValue() { 
			return value; 
		}
	}
	Scheduler scheduler = null;
	HashMap<Integer, View> componet_container = null;
	HashMap<Integer, Object> data_container = null;
	protected Activity  context = this;
	public Handler handler = new Handler();
	boolean isfinish=false;
	
	
//	액티비티를 촉화한다 중지했다가 재시작하는 경우라면 액티비티의 이전 상태 정보인 번틀이 전달된다 이정보대로 재초기화된다
//	-	엑티비티가  처음 시작될때 인자로null 값을 넘겨받으면서 호출된다 (시스템재시작경우)
//	-	액티비티가 실행된 적이 있는데 어떤 이유건 종료됐다가 다시실행되는경우 onSaveInstanceState() 메서드로 보관해둔 Bundle 인스턴스를 넘겨받는다, 저장할때는  onRestoreInstanceState(Bundle)
//	-	액티비트가 실행중인데 하드웨어 상태(예를들어 가로체그 세로보기 모드변경)에 따라 다른 시소스를 지정해 개발한 경우 하드웨어 상태가 변경되면 액티비티가 새로 생성돼 실행되면서  onCreate() 메서드 호출된다.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
		if(isfinish==false)
		onPretreatment();
		
		int themeget_style = AndroidUtility.requestGetParameter_toInt(context, REQUEST.THEME.getValue(), 0xffffffff);
		if(themeget_style !=0xffffffff)
			setTheme(themeget_style);
		super.onCreate(savedInstanceState);
		
		
		if(isfinish==false)
		onSettingComponent();
		if(isfinish==false)
		onSyncComponentData();
		if(isfinish==false)
		onAddComponentListener();
		if(isfinish==false)
		onPostProcessing();
	}

	
	public void onPretreatment(){}; // 전처리.데이터등..
	abstract public void onSettingComponent();  // 컴포넌트셋팅
	abstract public void onSyncComponentData(); // 컴포넌트 내용싱크
	abstract public void onAddComponentListener(); // 컴포넌트 리스너 등록
	public void onPostProcessing(){}; // 후처리.데이터등..
	
	final public void action(int request){action(request,null);}						//질문
	abstract public void action(int request,Object obj);// {}						//질문
	final public void actionForResult(int requst,int result) {actionForResult(requst, result, null);}	//응답
	abstract public void actionForResult(int requst,int result,Object obj);// {}	//응답
	
	
	
	
	
	
	
	public Scheduler getScheduler() {
		if(scheduler==null)
			scheduler=new Scheduler();
		return scheduler;
	}




	public void changeView(int requst) {}
	public View getComponent(int id) {
		if (componet_container == null) {
			componet_container = new HashMap<Integer, View>();
		}
		if (componet_container.get(new Integer(id)) == null) {
			componet_container.put(new Integer(id), context.findViewById(id));
		}
		return componet_container.get(new Integer(id));
	}
	public void setComponent(View view, int id) {
		view.setId(id);
		if (componet_container == null) {
			componet_container = new HashMap<Integer, View>();
		}
			componet_container.put(new Integer(id),view);
	}
	public Object getData(int id) {
		if (data_container == null) {
			data_container = new HashMap<Integer, Object>();
		}
		return data_container.get(new Integer(id));
	}
	public void setData(int id,Object object) {
		if (data_container == null) {
			data_container = new HashMap<Integer, Object>();
		}
			data_container.put(new Integer(id), object);
	
	}
	
	@Override
	public void finish() {
		isfinish=true;
		super.finish();
	}
	public void setfinish(boolean wantfinish){
		isfinish=wantfinish;
	}
	
	public void ok(View o){
		setResult(RESULT_OK);
		this.finish();
	}
	public void cancel(View o){
		setResult(RESULT_CANCELED);
		this.finish();
	}
	public void onClick(View o){
		action(o.getId(),o);
	}

	
	
	
//	액티비트가 재시작될 때 호출된다 특별히 할일은없다	
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	
//	액티비트가 사용자에게 보이기 직전에 호출된다
	@Override
	protected void onStart() {
		super.onStart();
	}
//	사용자와 상호작용을 하기 직전에 호출된다 이단계에서 스택의 제일 위로올라온다
//	정지상태에서->다시 시작될때 ,
//	액티비트가 처음 실행될때.
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	
//	다른 액티비트가 실행될 때 호출된다 이 단계에서 미저장한 데이터가 있으면 저장하고 애니메이션은 중지해야한다 이메스다가 리턴되어야 새애기비트가 활성화되므로 시간을 너무 많이 끌어선 안된다 
//	전면에서 뒤로 물러설때.
	@Override
	protected void onPause() {
		super.onPause();
	}
//	액티비트가 사용자에게 보이지 않게 될 때 호출한다
//	액티비트가 정지될때
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
//	액티비티가 파괴될 때 호출된다 시스템에 의해 강제 종료되는것인지 아니면 finish메서드 호출에 의해 스스로 종료되는것인지는 isFinishing 메서드로 조사 가능하다.
//	Finish() 호출해서 죽는거는 메모리가 부족해서 죽는거다.
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	//백버튼눌렀을때 보통여기에 걸어서 프로세스죽이지.
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}
