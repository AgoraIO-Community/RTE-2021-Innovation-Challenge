using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
    public sealed class ImageMessage : IMessage {
		private Int64 _Size = 0;
		private string _MediaId = "";
		private byte[] _Thumbnail = null;
		private string _FileName = "";
		private int _Width = 0;
		private int _Height = 0;
		private int _ThumbnailWidth = 0;
		private int _ThumbnailHeight = 0;

        public ImageMessage(IntPtr MessagePtr) {
            _MessagePtr = MessagePtr;
        }

		public ImageMessage(IntPtr MessagePtr, MESSAGE_FLAG MessageFlag) {
			_MessageFlag = MessageFlag;
			_MessagePtr = MessagePtr;
		}

		public ImageMessage(ImageMessage imageMessage, MESSAGE_FLAG MessageFlag) {
			_MessageFlag = MessageFlag;
			_MessageId = imageMessage.GetMessageId();
			_MessageType = imageMessage.GetMessageType();
			_MessageText = imageMessage.GetText();
			_IsOfflineMessage = imageMessage.IsOfflineMessage();
			_Ts = imageMessage.GetServerReceiveTs();
			_RawMessageData = imageMessage.GetRawMessageData();
			_Length = imageMessage.GetRawMessageLength();
			_Size = imageMessage.GetSize();
			_MediaId = imageMessage.GetMediaId();
			_FileName = imageMessage.GetFileName();
			_Width = imageMessage.GetWidth();
			_Height = imageMessage.GetHight();
			_ThumbnailWidth = imageMessage.GetThumbnailWidth();
			_ThumbnailHeight = imageMessage.GetThumbnailHeight();
		}

        ~ImageMessage() {
            Release();
        }

		/// <summary>
		/// Gets the size of the uploaded image.
		/// </summary>
		/// <returns>The size of the uploaded image in bytes.</returns>
        public Int64 GetSize() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Size;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return iImage_message_getSize(_MessagePtr);
        }

		/// <summary>
		/// Gets the media ID of the uploaded image.
		/// </summary>
		/// <returns>
		/// The media ID is automatically populated once the file is uploaded to the file server.
		/// The media ID is valid for 7 days because the Agora server keeps all uploaded images for 7 days only.
		/// </returns>
        public string GetMediaId() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE) 
				return _MediaId;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
            IntPtr mediaIdPtr = iFile_message_getMediaId(_MessagePtr);
            if (!ReferenceEquals(mediaIdPtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(mediaIdPtr);
			} else {
				return "";
			}
        }
		
		/// <summary>
		/// Sets the thumbnail of the uploaded image.
		/// </summary>
		/// <param name="thumbnail">The thumbnail of the uploaded image.</param>
        public void SetThumbnail(byte[] thumbnail) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE) {
				_Thumbnail = thumbnail;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setThumbnail(_MessagePtr, thumbnail, thumbnail.Length);
        }

		/// <summary>
		/// Gets the thumbnail data of the uploaded image.
		/// </summary>
		/// <returns>The thumbnail data of the uploaded image.</returns>
        public byte[] GetThumbnail() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Thumbnail;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return _Thumbnail;
			}
			long Length = iImage_message_getThumbnailLength(_MessagePtr);
			byte [] rawData = new byte[Length];
			IntPtr _ThumbnailData = iImage_message_getThumbnailData(_MessagePtr);
			Marshal.Copy(_ThumbnailData, rawData, 0, (int)Length);
            return rawData;
        }

		/// <summary>
		/// Sets the filename of the uploaded image.
		/// </summary>
		/// <param name="fileName">	The filename of the uploaded image. The total size of thumbnail and fileName must not exceed 32 KB.</param>
        public void SetFileName(string fileName) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_FileName = fileName;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setFileName(_MessagePtr, fileName);
        }

		/// <summary>
		/// Gets the filename of the uploaded image.
		/// </summary>
		/// <returns>The filename of the uploaded image.</returns>
        public string GetFileName() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _FileName;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
            IntPtr fileNamePtr = iImage_message_getFileName(_MessagePtr);
            if (!ReferenceEquals(fileNamePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(fileNamePtr);
			} else {
				return "";
			}
        }

		/// <summary>
		/// Sets the width of the uploaded image.
		/// </summary>
		/// <param name="width">The width of the uploaded image.</param>
        public void SetWidth(int width) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_Width = width;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setWidth(_MessagePtr, width);
        }

		/// <summary>
		/// Gets the width of the uploaded image.
		/// </summary>
		/// <returns>The width of the uploaded image. Returns 0 if the SDK does not support the format of the uploaded image.</returns>
        public int GetWidth() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Width;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return iImage_message_getWidth(_MessagePtr);
        }

		/// <summary>
		/// Sets the height of the uploaded image.
		/// </summary>
		/// <param name="height">The height of the uploaded image. Returns 0 if the SDK does not support the format of the uploaded image.</param>
        public void SetHeight(int height) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_Height = height;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setHeight(_MessagePtr, height);
        }

		/// <summary>
		/// Gets the height of the uploaded image.
		/// </summary>
		/// <returns>The height of the uploaded image.</returns>
        public int GetHight() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Height;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return iImage_message_getHeight(_MessagePtr);
        }

		/// <summary>
		/// Sets the width of the thumbnail.
		/// </summary>
		/// <param name="thumbnailWidth">the The width of the thumbnail.</param>
        public void SetThumbnailWidth(int thumbnailWidth) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_ThumbnailWidth = thumbnailWidth;
				return;
			}


			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setThumbnailWidth(_MessagePtr, thumbnailWidth);
        }

		/// <summary>
		/// Gets the width of the thumbnail.
		/// </summary>
		/// <returns>The width of the thumbnail.</returns>
        public int GetThumbnailWidth() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _ThumbnailWidth;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return iImage_message_getThumbnailWidth(_MessagePtr);
        }

		/// <summary>
		/// Sets the height of the thumbnail.
		/// </summary>
		/// <param name="height">The height of the thumbnail.</param>
        public void SetThumbnailHeight(int height) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_ThumbnailHeight = height;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
            iImage_message_setThumbnailHeight(_MessagePtr, height);
        }

		/// <summary>
		/// Gets the height of the thumbnail.
		/// </summary>
		/// <returns>The height of the thumbnail.</returns>
        public int GetThumbnailHeight() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _ThumbnailHeight;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return iImage_message_getThumbnailHeight(_MessagePtr);
        }
    }
}