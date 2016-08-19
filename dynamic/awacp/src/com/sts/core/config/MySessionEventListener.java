package com.sts.core.config;

import org.eclipse.persistence.sessions.DatasourceLogin;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;

import com.sts.core.util.SecurityEncryptor;

public class MySessionEventListener extends SessionEventAdapter {

	public void preLogin(SessionEvent event) {
        Session session = event.getSession();
        DatasourceLogin login = (DatasourceLogin)session.getDatasourceLogin();
        login.setEncryptionClassName(SecurityEncryptor.class.getName());
    }

}
