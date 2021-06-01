using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
	public sealed class RtmChannelMember : IRtmApiNative {
		private string _UserId {
			get;
			set;
		}

		private string _ChannelId {
			get;
			set;
		}
		public RtmChannelMember(string userId, string channelId) {
			_UserId = userId;
			_ChannelId = channelId;
		}

		/// <summary>
		/// Retrieves the user ID of a user in the channel.
		/// </summary>
		/// <returns>User ID of a user in the channel.</returns>
		public string GetUserId() {
			return _UserId;
		}

		/// <summary>
		/// Retrieves the channel ID of the user.
		/// </summary>
		/// <returns>Channel ID of the user.</returns>
		public string GetChannelId() {
			return _ChannelId;
		}
	}
}
