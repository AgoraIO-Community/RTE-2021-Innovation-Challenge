export enum CommonResponse {
  JSONError= 'JSONError',
  DBFailure = 'DBFailure'
}

export enum LoginResponse {
  NonExistent = 'UserNonExistent',
  PasswordError = 'PasswordError'
}

export enum SignInResponse {
  NameConflict = 'NameConflict'
}
