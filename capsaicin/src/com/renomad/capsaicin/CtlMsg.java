package com.renomad.capsaicin;

import java.nio.ByteBuffer;
import java.util.UUID;
import com.renomad.capsaicin.Logger;

/** 
 *  Control message for data flow to server. Needs to
 * be encrypted and secure.  We want our communication
 * to be opaque, but fast.
 * see comments in code for capsaicin_server.c to better
 * understand the protocol. -BK 1/19/2014
 * @author Byron Katz
 */
public final class CtlMsg {

    private final CtlAct action;
    private final int sid;
    private final UUID vid;
    private final UUID uid;
    private final long offset;
    private final short sbytes;

    //TODO - BK - make sure that the following adheres to best
    //practices for thread safety
    //TODO - BK - set up a way to send encoded messages back and forth.
    //openssl?                         

    /**
     * Constructor method to creat a thread-safe object 
     * for control messages to server.
     * actions are set by the CtlAct static object.
     * @param sid The session id, for tracking our conversation.
     * @param action CtlAct.send or CtlAct.receive, etc.
     * @param vid the video (or whatever) we want, as a 16 byte UUID.
     * @param uid the user we are, as a 16 byte UUID.
     * @param origin location within the file to start the file pull.
     * @param sbytes an integer to say how much to
     * pull down. (probably make this a constant - should be 
     * less than 1500 bytes, from the best reading I've 
     * done on the subject. - BK 1/19/2014
     * TODO - BK 1/19/2014 - make sure this is totally thread safe.
     * for example, does it need to be synchronized?
     * ////TODO - BK - WIP - FINISH THIS PART!
     */
    public CtlMsg (int sid, CtlAct action, UUID vid, UUID uid,
				      long offset, short sbytes) {
	this.sid = sid;
	this.action = action;
	this.vid = vid;
	this.uid = uid;
	this.offset = offset;
	this.sbytes = sbytes;
    }

    /**
     * Thread safe message object
     * @return a throw-away byte array
     */
    public byte[] getMessage() {
	ByteBuffer mbuf = ByteBuffer.allocate(47); //current size
	mbuf.putInt(sid)
	    .put(action.getValue())
	    .putLong(vid.getMostSignificantBits())
	    .putLong(vid.getLeastSignificantBits())
	    .putLong(uid.getMostSignificantBits())
	    .putLong(uid.getLeastSignificantBits())
	    .putLong(offset)
	    .putShort(sbytes);
	return mbuf.array();
    }

    @Override
    public String toString() {
	return "action: " + action + " sid: " + sid + " vid: " + vid +
	    "uid: " + uid + " offset: " + offset + " sbytes: " + sbytes;
    }

}
