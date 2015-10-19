package de.eleon.properties;

import com.google.common.base.Preconditions;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class PropertyBinder {

    private static ThreadLocal<Property> referenceGrabber = new ThreadLocal<>();

    public static <BO, T> Property<BO, T> property(Object o) {
        checkNotNull(referenceGrabber.get(), "Usage ... TODO");

        return detachReference();
    }

    public static <T> T from(final Class<T> tClass) {
        checkState(referenceGrabber.get() == null, "Race Condition with threadlocal!!!");

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(tClass);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(final Object o, final Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                attachReference(tClass, method);
                return null;
            }
        });
        return (T) enhancer.create();
    }

    private static <BO, T> Property<BO, T> detachReference() {
        Property<BO, T> ret = referenceGrabber.get();
        referenceGrabber.remove();
        return ret;
    }

    private static void attachReference(final Class<?> clazz, final Method method) {
        String propertyName = propertyNameFor(method.getName());
        try {
            final Field field = clazz.getDeclaredField(propertyName);
            field.setAccessible(true);

            Property property = new Property() {

                @Override
                public void set(Object bindingObject, Object value) {
                    try {
                        field.set(bindingObject, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public Object get(Object bindingObject) {
                    try {
                        return field.get(bindingObject);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
            referenceGrabber.set(property);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private static String propertyNameFor(String getterName) {
        checkState(isAGetter(getterName), getterName + " is not a valid getter");

        getterName = getterName.startsWith("is") ? getterName.substring(2) : getterName.substring(3);
        return Introspector.decapitalize(getterName);
    }

    private static boolean isAGetter(String methodName) {
        return methodName.startsWith("is") || methodName.startsWith("get");
    }

}
