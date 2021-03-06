/*
 * Copyright (C) 2004 Felipe Gustavo de Almeida
 * Copyright (C) 2010-2014 The MPDroid Project
 *
 * All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice,this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.a0z.mpd;

import org.a0z.mpd.exception.MPDServerException;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class representing a connection to MPD Server.
 */
class MPDConnectionMonoSocket extends MPDConnection {

    private Socket mSocket;

    private InputStreamReader mInputStream;

    private OutputStreamWriter mOutputStream;

    MPDConnectionMonoSocket(final InetAddress server, final int port, final String password,
            final int readWriteTimeout) throws MPDServerException {
        super(server, port, password, readWriteTimeout);
        // connect right away and setup streams
        connect();
    }

    @Override
    public InputStreamReader getInputStream() {
        return mInputStream;
    }

    @Override
    public void setInputStream(final InputStreamReader inputStream) {
        mInputStream = inputStream;
    }

    @Override
    public OutputStreamWriter getOutputStream() {
        return mOutputStream;
    }

    @Override
    public void setOutputStream(final OutputStreamWriter outputStream) {
        mOutputStream = outputStream;
    }

    @Override
    protected Socket getSocket() {
        return mSocket;
    }

    @Override
    protected void setSocket(final Socket socket) {
        mSocket = socket;
    }
}
