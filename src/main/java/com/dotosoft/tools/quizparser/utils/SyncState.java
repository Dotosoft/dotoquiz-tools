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

package com.dotosoft.tools.quizparser.utils;


/**
 * Synchronised object used to marshal updates and status
 * between the worker thread and the dispatcher thread
 */
public class SyncState {

    private final Object lock = new Object();

    private boolean cancelled;
    private String lastStatus;
    private boolean syncInProgress;
    private int totalDownloaded;
    private int totalUploaded;

    public boolean getIsInProgress() {
        synchronized ( lock ){
            return syncInProgress;
        }
    }

    public void addStats( int downloaded, int uploaded )
    {
        synchronized( lock ){
            totalDownloaded += downloaded;
            totalUploaded += uploaded;
        }
    }
    public boolean getIsCancelled() {
        synchronized ( lock ){
            return cancelled;
        }
    }

    public void setStatus( String msg ){
        synchronized ( lock ){
            lastStatus = msg;
        }
    }

    public void start() {
        synchronized (lock ){
            totalDownloaded = 0;
            totalUploaded = 0;
            cancelled = false;
            syncInProgress = true;
        }
    }

    public void cancel( boolean withError ){
        synchronized ( lock ){
            cancelled = true;
            syncInProgress = false;
        }
    }
}
