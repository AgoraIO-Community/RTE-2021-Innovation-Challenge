import styled from 'styled-components'

export const Setting = styled.div`
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

export const Block = styled.div`
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
  &:not(:first-child) {
    margin-top: 20px;
  }

  &:hover {
    box-shadow: 0 1em 4em 0.5em #d7d7d7;
    transform: translateY(-2px);
  }
`

export const Title = styled.div`
  font-size: 18px;
  color: #333;
  font-weight: bold;
  width: 100%;
  border-bottom: 1px solid #d1d9e6;
  padding-bottom: 10px;
`

export const Content = styled.div`
  margin-top: 10px;
  width: 100%;
  display: flex;
`

export const Item = styled.div`
  margin-right: 15px;
  font-size: 15px;
`
