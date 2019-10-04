/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

/**
 * The Objective-C runtime is a runtime library that provides support for the
 * dynamic properties of the Objective-C language, and as such is linked to by
 * all Objective-C apps. Objective-C runtime library support functions are
 * implemented in the shared library found at /usr/lib/libobjc.A.dylib.
 *
 * You typically don't need to use the Objective-C runtime library directly when
 * programming in Objective-C. This API is useful primarily for developing
 * bridge layers between Objective-C and other languages, or for low-level
 * debugging.
 *
 * The macOS implementation of the Objective-C runtime library is unique to the
 * Mac. For other platforms, the GNU Compiler Collection provides a different
 * implementation with a similar API. This document covers only the macOS
 * implementation.
 *
 * The low-level Objective-C runtime API is significantly updated in OS X
 * version 10.5. Many functions and all existing data structures are replaced
 * with new functions. The old functions and structures are deprecated in 32-bit
 * and absent in 64-bit mode. The API constrains several values to 32-bit ints
 * even in 64-bit mode—class count, protocol count, methods per class, ivars per
 * class, arguments per method, sizeof(all arguments) per method, and class
 * version number. In addition, the new Objective-C ABI (not described here)
 * further constrains sizeof(anInstance) to 32 bits, and three other values to
 * 24 bits—methods per class, ivars per class, and sizeof(a single ivar).
 * Finally, the obsolete NXHashTable and NXMapTable are limited to 4 billion
 * items.
 *
 * @author shannah (original)
 * @author patrickangle (2019 update to static native, added documentation,
 * etc.)
 */
public class ObjCRuntime {

    // TODO: Add documetnation per method from https://developer.apple.com/documentation/objectivec/objective-c_runtime
    
    static {
        // Register this class as being a native library.
        Native.register("objc.A");
    }

    public static native Pointer objc_lookUpClass(String name);

    public static native String class_getName(Pointer id);

    public static native Pointer class_getProperty(Pointer cls, String name);

    public static native Pointer class_getSuperclass(Pointer cls);

    public static native int class_getVersion(Pointer theClass);

    public static native String class_getWeakIvarLayout(Pointer cls);

    public static native boolean class_isMetaClass(Pointer cls);

    public static native int class_getInstanceSize(Pointer cls);

    public static native Pointer class_getInstanceVariable(Pointer cls, String name);

    public static native Pointer class_getInstanceMethod(Pointer cls, Pointer aSelector);

    public static native Pointer class_getClassMethod(Pointer cls, Pointer aSelector);

    public static native String class_getIvarLayout(Pointer cls);

    public static native Pointer class_getMethodImplementation(Pointer cls, Pointer name);

    public static native Pointer class_getMethodImplementation_stret(Pointer cls, Pointer name);

    public static native Pointer class_replaceMethod(Pointer cls, Pointer name, Pointer imp, String types);

    public static native Pointer class_respondsToSelector(Pointer cls, Pointer sel);

    public static native void class_setIvarLayout(Pointer cls, String layout);

    public static native Pointer class_setSuperclass(Pointer cls, Pointer newSuper);

    public static native void class_setVersion(Pointer theClass, int version);

    public static native void class_setWeakIvarLayout(Pointer cls, String layout);

    public static native String ivar_getName(Pointer ivar);

    public static native long ivar_getOffset(Pointer ivar);

    public static native String ivar_getTypeEncoding(Pointer ivar);

    public static native String method_copyArgumentType(Pointer method, int index);

    public static native String method_copyReturnType(Pointer method);

    public static native void method_exchangeImplementations(Pointer m1, Pointer m2);

    public static native void method_getArgumentType(Pointer method, int index, Pointer dst, long dst_len);

    public static native Pointer method_getImplementation(Pointer method);

    public static native Pointer method_getName(Pointer method);

    public static native int method_getNumberOfArguments(Pointer method);

    public static native void method_getReturnType(Pointer method, Pointer dst, long dst_len);

    public static native String method_getTypeEncoding(Pointer method);

    public static native Pointer method_setImplementation(Pointer method, Pointer imp);

    public static native Pointer objc_allocateClassPair(Pointer superclass, String name, long extraBytes);

    public static native Pointer[] objc_copyProtocolList(Pointer outCount);

    public static native Pointer objc_getAssociatedObject(Pointer object, String key);

    public static native Pointer objc_getClass(String name);

    public static native int objc_getClassList(Pointer buffer, int bufferlen);

    public static native Pointer objc_getFutureClass(String name);

    public static native Pointer objc_getMetaClass(String name);

    public static native Pointer objc_getProtocol(String name);

    public static native Pointer objc_getRequiredClass(String name);

    public static native long objc_msgSend(Pointer theReceiver, Pointer theSelector, Object... arguments);

    public static native long objc_msgSendSuper(Pointer superClassStruct, Pointer op, Object... arguments);

    public static native long objc_msgSendSuper_stret(Pointer superClassStruct, Pointer op, Object... arguments);

    public static native double objc_msgSend_fpret(Pointer self, Pointer op, Object... arguments);

    public static native void objc_msgSend_stret(Pointer stretAddr, Pointer theReceiver, Pointer theSelector, Object... arguments);

    public static native void objc_registerClassPair(Pointer cls);

    public static native void objc_removeAssociatedObjects(Pointer object);

    public static native void objc_setAssociatedObject(Pointer object, Pointer key, Pointer value, Pointer policy);

    public static native void objc_setFutureClass(Pointer cls, String name);

    public static native Pointer object_copy(Pointer obj, long size);

    public static native Pointer object_dispose(Pointer obj);

    public static native Pointer object_getClass(Pointer object);

    public static native String object_getClassName(Pointer obj);

    public static native Pointer object_getIndexedIvars(Pointer obj);

    public static native Pointer object_getInstanceVariable(Pointer obj, String name, Pointer outValue);

    public static native Pointer object_getIvar(Pointer object, Pointer ivar);

    public static native Pointer object_setClass(Pointer object, Pointer cls);

    public static native Pointer object_setInstanceVariable(Pointer obj, String name, Pointer value);

    public static native void object_setIvar(Pointer object, Pointer ivar, Pointer value);

    public static native String property_getAttributes(Pointer property);

    public static native boolean protocol_conformsToProtocol(Pointer proto, Pointer other);

    public static native Structure protocol_copyMethodDescriptionList(Pointer protocol, boolean isRequiredMethod, boolean isInstanceMethod, Pointer outCount);

    public static native Pointer protocol_copyPropertyList(Pointer proto, Pointer outCount);

    public static native Pointer protocol_copyProtocolList(Pointer proto, Pointer outCount);

    public static native Pointer protocol_getMethodDescription(Pointer proto, Pointer aSel, boolean isRequiredMethod, boolean isInstanceMethod);

    public static native String protocol_getName(Pointer proto);

    public static native Pointer protocol_getProperty(Pointer proto, String name, boolean isRequiredProperty, boolean isInstanceProperty);

    public static native boolean protocol_isEqual(Pointer protocol, Pointer other);

    public static native String sel_getName(Pointer aSelector);

    public static native Pointer sel_getUid(String name);

    public static native boolean sel_isEqual(Pointer lhs, Pointer rhs);

    public static native Pointer sel_registerName(String name);
}
