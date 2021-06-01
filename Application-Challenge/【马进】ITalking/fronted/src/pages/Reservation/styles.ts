import styled from 'styled-components'

export const Reservation = styled.div`
  flex: 0.5;
  min-width: fit-content;
  overflow-y: auto;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
  font-size: 18px;

  &::-webkit-scrollbar {
    display: none;
  }

  @media screen and (max-width: 850px) {
    flex: 0.9;
  }
  @media screen and (max-width: 570px) {
    flex: 1;
  }
`

export const ReservationHeader = styled.div`
  padding: 20px;
  display: flex;
  justify-content: space-between;
`

export const ReservationTitle = styled.div`
  height: 40px;
  display: flex;
  align-items: center;
  color: #333;
`

export const CreateReservationBtn = styled.div`
  svg {
    fill: #fff;
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 1rem;
    flex-shrink: 0;
    user-select: none;
    margin-left: 5px;
  }
`

export const RoomList = styled.div`
  margin-bottom: 60px;
  display: flex;
  flex-direction: column;
`

export const RoomItem = styled.div`
  display: flex;
  flex-direction: column;
  padding: 20px;
  margin: 10px;
  cursor: pointer;
  border-radius: 10px;
  background: hsla(0, 0%, 100%, 0.6);
  box-shadow: 0 0.625em 3.75em 0 #eaeaea;
  transition: all 0.25s ease 0.2s, transform 0.3s cubic-bezier(0.6, 0.2, 0.1, 1) 0.1s;
  user-select: none;
  position: relative;

  &:not(:first-child) {
    margin-top: 20px;
  }

  &:hover {
    box-shadow: 0 1em 4em 0.5em #d7d7d7;
    transform: translateY(-2px);
  }
`

export const RoomDetail = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
`

export const RoomName = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: 700;
  color: #333;
`

export const RoomDescription = styled.div`
  margin-top: 8px;
  font-size: 15px;
  color: #5f5e5e;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
`

export const RoomContent = styled.div`
  height: 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
`

export const RoomCreator = styled.div`
  display: flex;
`

export const CreatorAvatar = styled.div`
  margin-right: 10px;
  height: 32px;
  width: 32px;
`

export const CreatorName = styled.div`
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 100%;
  font-size: 14px;
  color: rgb(34 43 56);
  display: flex;
  align-items: center;
`

export const RoomTime = styled.div`
  text-transform: uppercase;
  font-size: 14px;
  color: #fd4d4d;
  position: absolute;
  right: 20px;
`

export const RoomBtnGroup = styled.div`
  display: flex;
`

export const RoomBtn = styled.div`
  &:not(:last-child) {
    margin-right: 10px;
  }
`
