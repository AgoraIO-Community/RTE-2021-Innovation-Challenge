import styled from 'styled-components'

export const MemberList = styled.div`
  display: flex;
  width: 100%;
  align-items: center;
`

export const Name = styled.span`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  font-size: 14px;
  color: rgb(34 43 56)
`

export const AvatarList = styled.div`
  margin-right: 10px;
  display: flex;
`

export const Avatar = styled.span`
  overflow: hidden;
  
  img {
    display: inline-block;
    border-radius: 9999px;
    border-color: rgb(21, 26, 33);
    background-color: rgb(21, 26, 33);
  }
`
