package com.kangraoo.basektlib.tools

import android.text.TextUtils
import com.kangraoo.basektlib.data.model.BParam
import com.kangraoo.basektlib.tools.log.ULog.e
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.*

/**
 * 反射工具类
 * 通过反射获得对应函数功能
 */
object UReflection {
    private const val TAG = "UReflection"

    /**
     * 调用Class的静态方法
     */
    fun <T> invokeClassMethod(
        classPath: String,
        methodName: String,
        paramClasses: Array<Class<*>>,
        params: Array<Any>
    ): T? {
        return executeClassLoad(getClass(classPath), methodName, paramClasses, params) as T?
    }

    /**
     * 调用Class的无参静态方法
     */
    fun invokeMethod(obj: Any?, methodName: String?, params: Array<Any>?): Any? {
        return invokeMethod(obj, methodName, null, params)
    }

    /**
     * 通过类对象，运行指定方法
     *
     * @param obj 类对象
     * @param methodName 方法名
     * @param paramTypes 参数对应的类型（如果不指定，那么从params来判断，可能会判断不准确，例如把CharSequence 判断成String，导致反射时方法找不到）
     * @param params 参数值
     * @return 失败返回null
     */
    fun invokeMethod(
        obj: Any?,
        methodName: String?,
        paramTypes: Array<Class<*>?>?,
        params: Array<Any>?
    ): Any? {
        var paramTypes = paramTypes
        if (obj == null || TextUtils.isEmpty(methodName)) {
            return null
        }
        val clazz: Class<*> = obj.javaClass
        try {
            if (paramTypes == null) {
                if (params != null) {
                    paramTypes = arrayOfNulls<Class<*>?>(params.size)
                    for (i in params.indices) {
                        paramTypes[i] = params[i].javaClass
                    }
                }
            }
            val method = clazz.getMethod(methodName!!, *paramTypes!!)
            method.isAccessible = true
            return method.invoke(obj, *params!!)
        } catch (e: NoSuchMethodException) {
            e(e, e.message)
        } catch (e: IllegalAccessException) {
            e(e, e.message)
        } catch (e: InvocationTargetException) {
            e(e, e.message)
        }
        return null
    }

    /**
     * 反射获取对象属性值
     */
    public fun getFieldValue(obj: Any?, fieldName: String?): Any? {
        if (obj == null || TextUtils.isEmpty(fieldName)) {
            return null
        }
        var clazz: Class<*>? = obj.javaClass
        while (clazz != Any::class.java) {
            try {
                val field = clazz!!.getDeclaredField(fieldName!!)
                field.isAccessible = true
                return field[obj]
            } catch (e: IllegalAccessException) {
                e(e, e.message)
            } catch (e: NoSuchFieldException) {
                e(e, e.message)
            }
            clazz = clazz!!.superclass
        }
        return null
    }

    /**
     * 反射修改对象属性值
     */
    fun setFieldValue(obj: Any?, fieldName: String?, value: Any?) {
        if (obj == null || TextUtils.isEmpty(fieldName)) {
            return
        }
        var clazz: Class<*>? = obj.javaClass
        while (clazz != Any::class.java) {
            try {
                val field = clazz!!.getDeclaredField(fieldName!!)
                field.isAccessible = true
                field[obj] = value
                return
            } catch (e: IllegalAccessException) {
                e(e, e.message)
            } catch (e: NoSuchFieldException) {
                e(e, e.message)
            }
            clazz = clazz!!.superclass
        }
    }

    /**
     * 获取Class所有的静态成员
     */
    fun getClassStaticField(clazz: Class<*>): List<Field> {
        val fields = clazz.fields
        val ret: MutableList<Field> = ArrayList()
        for (field in fields) {
            val m = Modifier.toString(field.modifiers)
            if (m.contains("static")) {
                ret.add(field)
            }
        }
        return ret
    }

    /**
     * 获取Class所有的成员，包括父类，子类,必须继承SModel
     * @param clazz
     * @return
     */
    fun getClassField(clazz: Class<*>): List<Field> {
        if (!BParam::class.java.isAssignableFrom(clazz)) {
            throw ClassCastException("clazz 必须继承BParam")
        }
        val ret: MutableList<Field> = ArrayList()
        var tempClass: Class<*>? = clazz
        while (tempClass != null && !tempClass.isAssignableFrom(BParam::class.java)) { // 当父类为SModel的时候说明到达了最上层的父类(BParam).
            ret.addAll(Arrays.asList(*tempClass.declaredFields))
            tempClass = tempClass.superclass // 得到父类,然后赋给自己
        }
        return ret
    }

    /**
     * 获取Class所有静态字段的值
     */
    fun getClassStaticFieldValue(clazz: Class<*>): List<Any> {
        val fields = clazz.fields
        val ret: MutableList<Any> = ArrayList()
        for (field in fields) {
            val m = Modifier.toString(field.modifiers)
            if (m.contains("static")) {
                try {
                    ret.add(field[null])
                } catch (e: IllegalAccessException) {
                    e(e, e.message)
                }
            }
        }
        return ret
    }

    /**
     * 获取某个字段的@annotation
     */
    fun <A : Annotation?> getFieldAnnotationsByType(field: Field, annotationType: Class<A>?): A {
        return field.getAnnotation(annotationType)
    }

    /**
     * 判断某个类是否包含某个方法，前提是这个类也存在
     */
    fun hasMethod(classPath: String, methodName: String, paramClasses: Array<Class<*>>): Boolean {
        return getMethod(getClass(classPath), methodName, paramClasses) != null
    }

    /**
     * 反射修改类的私有静态final变量(常量)的值
     * 注意：对于基本类型的静态常量，JAVA在编译的时候就会把代码中对此常量中引用的地方替换成相应常量值。
     * 结论：反射失效的情会发生在int、long、boolean以及String这些基本类型上，而如果把类型改成Integer、Long、Boolean这种包装类型，或者其他诸如Date、Object都不会出现失效的情况。
     */
    fun setFinalStaticField(clazz: Class<*>, fieldName: String?, newValue: Any?) {
        try {
            // 获取要反射的类字段
            val field = clazz.getDeclaredField(fieldName!!)
            // 打开字段的访问权权限，去除private修饰符的影响
            field.isAccessible = true
            // 去除final修饰符的影响，将字段设为可修改的
//            Field modifiersField = Field.class.getDeclaredField("modifiers");
//            modifiersField.setAccessible(true);
//            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            // 设置新的值
            field[null] = newValue
        } catch (e: IllegalAccessException) {
            e(e, e.message)
        } catch (e: NoSuchFieldException) {
            e(e, e.message)
        }
    }

    /**
     * ****************************** basic ******************************
     */
    private fun getClass(str: String): Class<*>? {
        var cls: Class<*>? = null
        try {
            cls = Class.forName(str)
        } catch (e: ClassNotFoundException) {
            e(e, e.message)
        }
        return cls
    }

    private fun executeClassLoad(
        cls: Class<*>?,
        str: String,
        clsArr: Array<Class<*>>,
        objArr: Array<Any>
    ): Any? {
        var obj: Any? = null
        if (!(cls == null || checkObjExists(str))) {
            val method = getMethod(cls, str, clsArr)
            if (method != null) {
                method.isAccessible = true
                try {
                    obj = method.invoke(null, *objArr)
                } catch (e: IllegalAccessException) {
                    e(e, e.message)
                } catch (e: InvocationTargetException) {
                    e(e, e.message)
                }
            }
        }
        return obj
    }

    private fun getMethod(cls: Class<*>?, str: String, clsArr: Array<Class<*>>): Method? {
        val method: Method? = null
        return if (cls == null || checkObjExists(str)) {
            null
        } else try {
            cls.methods
            cls.declaredMethods
            cls.getDeclaredMethod(str, *clsArr)
        } catch (e: NoSuchMethodException) {
            e(e, e.message)
            try {
                cls.getMethod(str, *clsArr)
            } catch (e1: NoSuchMethodException) {
                e(e1, e1.message)
                if (cls.superclass != null) getMethod(cls.superclass, str, clsArr) else method
            }
        }
    }

    private fun checkObjExists(obj: Any?): Boolean {
        return obj == null || obj.toString() == "" || obj.toString().trim { it <= ' ' } == "null"
    }
}
