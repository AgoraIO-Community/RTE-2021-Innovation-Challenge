import styled from 'styled-components'
import { Input } from 'antd'

export const Search = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 10px;
`

export const SearchBar = styled.div`
  width: 100%;
  background-color: #ebeef0;
  padding: 10px;
  border-radius: 20px;
  height: 45px;

  .ant-input-affix-wrapper {
    padding: 0px !important;
    padding-left: 6.5px !important;
  }
`

export const SearchInput = styled(Input)`
  border: none;
  background-color: #ebeef0;
  color: rgb(91, 112, 131);

  &::placeholder {
    color: rgb(91, 112, 131) !important;
  }

  .ant-input::placeholder {
    color: rgb(91, 112, 131) !important;

  }

  svg {
    fill: rgb(91, 112, 131);
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 15px;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    user-select: none;
    margin-right: 5px;
  }
`

export const SearchStatus = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 220px;
  background-color: rgba(255,255,255,1.00);
  font-size: 18px;
  .ant-spin-dot {
    margin-bottom: 10px;
  }
`

export const TipPanel = styled.div<{visible: boolean}>`
  width: 28%;
  z-index: 3;
  position: absolute;
  top: 60px;
  max-height: 500px;
  border-radius: 4px;
  overflow-y: auto;
  display: ${props => props.visible ? 'block' : 'none'};
  background-color: rgba(255,255,255,1.00);
  overscroll-behavior: contain;
  box-shadow: rgb(101 119 134 / 20%) 0px 0px 15px, rgb(101 119 134 / 15%) 0px 0px 3px 1px;
`

export const TipList = styled.div`
  width: 100%;
`

export const TipItem = styled.div`
  cursor: pointer;
  border-bottom: solid 0.5px rgb(235, 238, 240);
  display: flex;
  align-items: center;
  position: relative;
  padding: 12px 16px;
  vertical-align: middle;
  transition: all 0.2s;
  &:hover {
    background: #f7f9fa;
  }
`
export const TipType = styled.div`
  margin: 0px 8px;
`

export const TipName = styled.div`
  font-size: 15px;
  color: rgb(15, 20, 25);
  font-weight: 700;
  display: flex;
  align-items: center;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`

export const TipAvatar = styled.div`
  margin-right: 12px;
  height: 40px;
  width: 40px;
`
