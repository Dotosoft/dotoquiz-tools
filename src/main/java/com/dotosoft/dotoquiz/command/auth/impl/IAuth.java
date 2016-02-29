package com.dotosoft.dotoquiz.command.auth.impl;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.chain.Context;

public interface IAuth {
	Object authenticate( Context context )  throws IOException, GeneralSecurityException;
}
