import styled from 'styled-components'

export const FollowBtn = styled.div`
  width: 100%;
  margin: 10px 0px;
  display: flex;
  justify-content: center;

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

  .anticon-user-add {
    svg {
      fill: rgba(0, 0, 0, 0.85);
      transition: all 0.3s cubic-bezier(0.645, 0.045, 0.355, 1);
    }
  }

  .ant-btn {
    &:hover,
    &:focus,
    &:visited {
      .anticon-user-add {
        svg {
          fill: #40a9ff
        }
      }
    }

    &:active {
      .anticon-user-add {
        svg {
          fill: #096dd9
        }
      }
    }
  }

`
