package com.jackstraw.market.util

import android.content.Context
import android.util.Base64
import com.jackstraw.market.JApplication
import java.io.*

/**
 * @author jackstraw
 * @date 2018/1/11
 * @describe Sp工具类
 */
object SpUtil {

    /**
     * 保存sp数据
     *
     * @param name 表名
     * @param key  键
     * @param t
     * @param <T>
    </T> */
    fun <T> saveToSP(name: String, key: String, t: T) {
        JApplication.appContext?.let {
            val sp = it.getSharedPreferences(name, Context.MODE_PRIVATE) ?: return
            when (t) {
                is Boolean -> sp.edit().putBoolean(key, t as Boolean).apply()
                is String -> sp.edit().putString(key, t as String).apply()
                is Int -> sp.edit().putInt(key, t as Int).apply()
                is Float -> sp.edit().putFloat(key, t as Float).apply()
                is Long -> sp.edit().putLong(key, t as Long).apply()
            }
        }
    }

    /**
     * 获取sp数据
     *
     * @param name         表名
     * @param key          键
     * @param defaultValue 默认返回的参数
     * @param <T>
     * @return
    </T> */
    fun <T> getFromSP(name: String, key: String, defaultValue: T): T {
        val context = JApplication.appContext ?: return defaultValue
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE) ?: return defaultValue
        val map = sp.all ?: return defaultValue
        return if (map[key] == null) {
            defaultValue
        } else map[key] as T
    }

    /**
     * 清除sp数据
     *
     * @param name
     * @return
     */
    fun clearToSP(name: String): Boolean {
        val context = JApplication.appContext ?: return false
        val sp = context.getSharedPreferences(name, Context.MODE_PRIVATE) ?: return false
        val editor = sp.edit()
        editor.clear()
        return editor.commit()
    }

    /**
     * 保存对象到sp < 对象需要序列化 implements Serializable >
     *
     * @param name
     * @param key
     * @param object
     */
    fun setObjectSP(name: String, key: String, `object`: Any) {
        val context = JApplication.appContext
        val sp = context?.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp?.let {
            //创建字节输出流
            val baos = ByteArrayOutputStream()
            //创建字节对象输出流
            var out: ObjectOutputStream? = null
            try {
                //然后通过将字对象进行64转码，写入key值为key的sp中
                out = ObjectOutputStream(baos)
                out.writeObject(`object`)
                val objectVal = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
                val editor = sp.edit()
                editor.putString(key, objectVal)
                editor.commit()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    baos.close()
                    if (out != null) {
                        out.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 获取sp中的对象 < 对象需要序列化 implements Serializable >
     *
     * @param name  表名
     * @param key   键
     * @param clazz 类
     * @param <T>   返回泛型
     * @return
    </T> */
    fun <T> getObjectSP(name: String, key: String, clazz: Class<T>): T? {
        val context = JApplication.appContext
        val sp = context?.getSharedPreferences(name, Context.MODE_PRIVATE)
        sp?.let {
            if (sp.contains(key)) {
                val objectVal = sp.getString(key, null)
                val buffer = Base64.decode(objectVal, Base64.DEFAULT)
                //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
                val bais = ByteArrayInputStream(buffer)
                var ois: ObjectInputStream? = null
                try {
                    ois = ObjectInputStream(bais)
                    return ois.readObject() as T
                } catch (e: StreamCorruptedException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: ClassNotFoundException) {
                    e.printStackTrace()
                } finally {
                    try {
                        bais.close()
                        ois?.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return null
    }
}
