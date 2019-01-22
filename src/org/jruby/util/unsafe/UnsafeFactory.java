/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: EPL 1.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Common Public
 * License Version 1.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/cpl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.util.unsafe;

import java.lang.reflect.Field;

public class UnsafeFactory {
    private static final Unsafe unsafe = loadUnsafe();
    private static final boolean DEBUG = false;

    private static Unsafe loadUnsafe() {
        Unsafe unsafe = null;
        // first try our custom-generated Unsafe
        try {
            Class unsafeClass = Class.forName("org.jruby.util.unsafe.GeneratedUnsafe");
            unsafe = (Unsafe)unsafeClass.newInstance();
        } catch (Throwable ignore) {
            if (DEBUG) ignore.printStackTrace();
        }
        
        // then try Sun's Unsafe
        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            // if we get here, the class and field exist; construct our Unsafe impl
            // that calls it directly
            unsafe = (Unsafe)Class.forName("org.jruby.util.unsafe.SunUnsafeWrapper").newInstance();
        } catch (Throwable ignore) {
            if (DEBUG) ignore.printStackTrace();
        }
        
        // else leave it null
        if (DEBUG && unsafe == null) System.err.println("No Unsafe implementation available");
        return unsafe;
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }
}
