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

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Patrick Angle
 */
public class Images {
    public static final Image fromClasspath(String classpath) {
        URL url = ClassLoader.getSystemResource(classpath);
        Toolkit kit = Toolkit.getDefaultToolkit();
        return kit.createImage(url);
    }
    
    public static final List<Image> fromClasspaths(List<String> classpaths) {
        List<Image> images = new ArrayList<>(classpaths.size());
        Toolkit kit = Toolkit.getDefaultToolkit();
        
        for (int i = 0; i < classpaths.size(); i++) {
            URL url = ClassLoader.getSystemResource(classpaths.get(i));
            images.add(kit.createImage(url));
        }
        
        return images;
    }
}
