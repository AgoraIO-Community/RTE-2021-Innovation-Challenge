package com.kangraoo.basektlib.tools.rx

import androidx.lifecycle.LifecycleOwner
import com.kangraoo.basektlib.tools.rx.RxTransformerHelper.destory
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.concurrent.ConcurrentHashMap

/**
 *
 * Sticky事件只指事件消费者在事件发布之后才注册的也能接收到该事件的特殊类型。
 * Android中就有这样的实例，也就是Sticky Broadcast，即粘性广播。
 * 正常情况下如果发送者发送了某个广播，而接收者在这个广播发送后才注册自己的Receiver，这时接收者便无法接收到刚才的广播，
 * 为此Android引入了StickyBroadcast，在广播发送结束后会保存刚刚发送的广播（Intent），
 * 这样当接收者注册完Receiver后就可以接收到刚才已经发布的广播。这就使得我们可以预先处理一些事件，让有消费者时再把这些事件投递给消费者。
 * Created by WaTaNaBe on 2017/9/1.
 */
class RxBus private constructor() {
    companion object {
        @JvmStatic
        val instance: RxBus by lazy {
            RxBus()
        }
    }

    private val mBus: Subject<Any> = PublishSubject.create<Any>().toSerialized()
    private val mStickyEventMap: MutableMap<Class<*>, Any> = ConcurrentHashMap()

    /**
     * 将数据添加到订阅
     * 这个地方是再添加订阅的地方。最好创建一个新的类用于数据的传递
     */
    fun post(obj: Any) {
        if (mBus.hasObservers()) { // 判断当前是否已经添加订阅
            mBus.onNext(obj)
        }
    }

    /**这个是传递集合如果有需要的话你也可以进行更改 */
    fun post(obj: List<Any?>) {
        if (mBus.hasObservers()) { // 判断当前是否已经添加订阅
            mBus.onNext(obj)
        }
    }

    /**
     * 注册，传递tClass的时候最好创建一个封装的类。这对数据的传递作用
     * 新更改仅仅抛出生成类和解析
     */
    fun <T> register(owner: LifecycleOwner, tClass: Class<T>?, consumer: Consumer<T>?): Disposable {
        return mBus.ofType(tClass).destory(owner).subscribe(consumer)
    }
    /**
     * Stciky 相关
     */
    /**
     * 发送一个新Sticky事件
     */
    fun postSticky(event: Any) {
        synchronized(mStickyEventMap) { mStickyEventMap.put(event.javaClass, event) }
        mBus.onNext(event)
    }

    /**
     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
     */
    fun <T> registerSticky(owner: LifecycleOwner, tClass: Class<T>, consumer: Consumer<T>?): Disposable {
        synchronized(mStickyEventMap) {
            val observable = mBus.ofType(tClass) // 获取发送事件的Observable
            val event = mStickyEventMap[tClass]
            return if (event != null) {
// 通过mergeWith合并两个Observable
                observable.mergeWith(Observable.create { emitter ->
                    emitter.onNext(
                        tClass.cast(event)!!
                    )
                }).destory(owner).subscribe(consumer)
            } else {
                observable.destory(owner).subscribe(consumer)
            }
        }
    }
    //    /**
    //     * 根据传递的 eventType 类型返回特定类型(eventType)的 被观察者
    //     */
    //    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
    //        synchronized (mStickyEventMap) {
    //            Observable<T> observable = mBus.ofType(eventType);
    //            final Object event = mStickyEventMap.get(eventType);
    //
    //            if (event != null) {
    //                return Observable.merge(observable, Observable.create(new Observable.OnSubscribe<T>() {
    //                    @Override
    //                    public void call(Subscriber<? super T> subscriber) {
    //                        subscriber.onNext(eventType.cast(event));
    //                    }
    //                }));
    //            } else {
    //                return observable;
    //            }
    //        }
    //    }
    /**
     * 根据eventType获取Sticky事件
     */
    fun <T> getStickyEvent(eventType: Class<T>): T? {
        synchronized(mStickyEventMap) { return eventType.cast(mStickyEventMap[eventType]) }
    }

    /**
     * 移除指定eventType的Sticky事件
     */
    fun <T> removeStickyEvent(eventType: Class<T>): T? {
        synchronized(mStickyEventMap) { return eventType.cast(mStickyEventMap.remove(eventType)) }
    }

    /**
     * 移除所有的Sticky事件
     */
    fun removeAllStickyEvents() {
        synchronized(mStickyEventMap) { mStickyEventMap.clear() }
    }

    /**
     * 确定接收消息的类型
     * @param aClass
     * @param <T>
     * @return
     * 下面为背压使用方式
    </T> */
    /*    public <T> Flowable<T> toFlowable(Class<T> aClass) {
        return mBus.ofType(aClass);
    }*/

    //    private final FlowableProcessor<Object> mBus;//背压测试
}
