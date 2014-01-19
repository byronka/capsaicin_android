package com.renomad.capsaicin;

import com.renomad.capsaicin.Logger;

	/** 
	  *  Control message for data flow to server. Needs to
	  * be encrypted and secure.  We want our communication
	  * to be opaque, but fast.
	  *
	  */
	public final class CtlMsg {

		private final byte action;
		private final byte id;

		//TODO - BK - make sure that the following adheres to best
		//practices for thread safety
		//TODO - BK - set up a way to send encoded messages back and forth.
		//openssl?
		/**
		  * Creates a thread-safe object for control messages to server.
		  * actions are set by the CtlAct static object.
		  * @param action CtlAct.send or CtlAct.receive
		  * @param id the video we want
		  */
		public CtlMsg(CtlAct action, byte id) {
			this.action = action.getValue();
			this.id = id;
		}

		/**
		  * Thread safe message object
		  * @return a throw-away byte array
		  */
		public byte[] getMessage() {
			Logger.log("returning a control message of action "+action+" and id "+id+".");
			byte[] msg = {action, id};
			return msg;
		}

		@Override
		public String toString() {
			return "action: " + action + " id: " + id;
		}

	}
