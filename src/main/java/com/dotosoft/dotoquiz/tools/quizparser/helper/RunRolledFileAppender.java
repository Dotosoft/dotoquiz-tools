/*
	Copyright 2015 Denis Prasetio
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/

package com.dotosoft.dotoquiz.tools.quizparser.helper;

import org.apache.log4j.RollingFileAppender;

/**
 This appender rolls over at program start.
 This is for creating a clean boundary between log data of different runs.
 */
public class RunRolledFileAppender extends RollingFileAppender
{
    public RunRolledFileAppender() { }

    @Override
    public void activateOptions() {
        super.activateOptions();
        super.rollOver();
    }

    @Override
    public void rollOver() { }

}