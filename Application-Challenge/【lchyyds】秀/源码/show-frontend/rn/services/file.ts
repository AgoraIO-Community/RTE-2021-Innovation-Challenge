import { request } from '../utils/request';

interface UploadFileParams {
  file: FormData,
  userId: string,
};

interface UploadFileResp {
  success: boolean,
  code: number,
  msg: string,
  data: string,
};

export const uploadFileService: (params: UploadFileParams) => Promise<UploadFileResp>
= ({ file, userId, }) => {

  return request(`/uploadFile/${userId}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data'
    },
    body: file,
    bodyNoJSON: true,
  });
};