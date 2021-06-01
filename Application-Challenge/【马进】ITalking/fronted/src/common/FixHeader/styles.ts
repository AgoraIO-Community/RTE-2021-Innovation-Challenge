import styled from 'styled-components'

export const FixHeader = styled.div`
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 15px 20px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  user-select: none;
  cursor: default;
  background-color: #fff;
  box-shadow: 0 0 3px #ccc;
`

export const FixTitle = styled.span`
  color: #1890ff;
  font-weight: bold;
  font-size: 20px;
  opacity: .8;
  svg {
    fill: #1890ff;
    width: 1em;
    height: 1em;
    display: inline-block;
    font-size: 1.3rem;
    transition: fill 200ms cubic-bezier(0.4, 0, 0.2, 1) 0ms;
    flex-shrink: 0;
    margin-left: 5px;
  }
`
