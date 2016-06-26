package com.example.android.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class MainActivity extends AppCompatActivity {

    private String[] mArray;
    private Subscriber<String> mSubscriber;
    private String[] mArray1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void click1(View view) {
        //创建被观察者Observable //可观察者对象
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");//可以多个onNext
                //subscriber.onNext("world");

                subscriber.onCompleted();//事件序列结束标记


               //subscriber.onError();//事件错误标记，通常结束标记与错误标记只能出现一个
            }
        }).subscribe(new Observer<String>() { //指定啦观察者，被观察者必须指定了观察者的整个事件流程才可调用；
            @Override
            public void onCompleted() {
                Toast.makeText(getApplicationContext(),"接受",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(getApplicationContext(),"发出",Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 被观察者变形
     * @param view
     */
    public void click2(View view) {
        Observable.just(10,12)
                .subscribe(new Observer<Integer>() { //观察者Observer
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),"completed",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Toast.makeText(getApplicationContext(),"onnext",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 订阅者变形
     * @param view
     */
    public void click3(View view) {
        mArray = new String[]{"url", "url"};
//Subscriber<可以是String 或int 或 long...>()
        mSubscriber = new Subscriber<String>() {//订阅者Subscriber 多一个方法是onstart方法

//            @Override
//            public void onStart() {
//                super.onStart();
            //当开始运行时调用这个方法
//            }

            @Override
            public void onCompleted() {
                Toast.makeText(getApplicationContext(),"接受",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(getApplicationContext(),"发出",Toast.LENGTH_SHORT).show();
            }
        };
        Observable.from(mArray).subscribe(mSubscriber);

    }



    public void click4(View view) {
        mArray1 = new String[]{"url"};
        Observable.from(mArray1).subscribe(
                new Action1<String>() {
                    @Override
                    public void call(String s) { //同上面的click1的onNext 闭包
                        Toast.makeText(getApplicationContext()," callString",Toast.LENGTH_SHORT).show();
                    }
                }, new Action1<Throwable>() {//同上面的click1的onError 闭包
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(getApplicationContext()," Action1"+throwable,Toast.LENGTH_SHORT).show();
                    }
                }, new Action0() {//同上面的click1的onCompleted 闭包
                    @Override
                    public void call() {
                        Toast.makeText(getApplicationContext()," Action0",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void click5(View view) {
                               //第一个参数为转换前，第二个为转换后参数
        Observable.just(6).map(new Func1<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return null;
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

            }
        });
    }
    /**
     * 在生命周期onstop中取消订阅者
     */
    @Override
    protected void onStop() {
        super.onStop();
        if(mSubscriber!=null&&!mSubscriber.isUnsubscribed()){
            mSubscriber.unsubscribe();//取消订阅
        }

    }


}
