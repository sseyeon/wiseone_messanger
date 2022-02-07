package com.messanger.engine.uc.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;

public class ProtocolCodecFactory extends DemuxingProtocolCodecFactory {

    public ProtocolCodecFactory() {
        super.register(RequestDecoder.class);
        super.register(ResponseEncoder.class);
    }
}
