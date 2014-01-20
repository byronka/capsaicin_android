package com.renomad.capsaicin;

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

		private final ByteBuffer buf;

		//TODO - BK - make sure that the following adheres to best
		//practices for thread safety
		//TODO - BK - set up a way to send encoded messages back and forth.
		//openssl?                         

		private CtlMsg(ByteBuffer[] buf) {
			this.buf = buf.clone();
		}

		/**
		  * Factory method to creat a thread-safe object 
			* for control messages to server.
		  * actions are set by the CtlAct static object.
		  * @param action CtlAct.send or CtlAct.receive, etc.
		  * @param id the video (or whatever) we want, as a 16 byte UUID.
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
		public static CtlMsg createCtlMsg(CtlAct action, UUID id, UUID uid
				long origin, short sbytes) {
			final int current_size_of_msg_packet = 43; //see capsaicin_server.c
			ByteBuffer mbuf = ByteBuffer.allocate(current_size_of_msg_packet);
			byte m_action = mbuf.put(0, action.getValue();
			mbuf.putLong(1,id.getMostSignificantBits());
			mbuf.putLong(9, id.getLeastSignificantBits();
			mbuf.putLong(17, uid.getMostSignificantBits();
			mbuf.putLong(25, uid.getLeastSignificantBits();
			mbuf.putLong(33, origin.getValue();
			mbuf.putLong(41, action.getValue();
			return new CtlMsg(mbuf);
		}

		/**
		  * Thread safe message object
		  * @return a throw-away byte array
		  */
		public byte[] getMessage() {
			Logger.log("returning a control message of action "+
					action+" and id "+id+".");
			byte[] msg = {action, id};
			return msg;
		}

		@Override
		public String toString() {
			return "action: " + action + " id: " + id;
		}

	}
