import { createGlobalStyle } from 'styled-components'

const Styles = createGlobalStyle`

  body,
  html,
  a,
  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    font-family: 'Helvetica Neue', 'Microsoft Yahei', -apple-system, sans-serif;;
  }


  body {
    margin: 0;
    padding: 0;
    border: 0;
    outline: 0;
    background: #fff;
    overflow-x: hidden;
  }

  a:hover {
    color: #000;
  }

  h1,
  h2,
  h3,
  h4,
  h5,
  h6 {
    color: #0a1f44;
    font-size: 2.575rem;
    line-height: 3.0625rem;

    @media only screen and (max-width: 414px) {
      font-size: 1.625rem;
    }
  }

  p {
    color: #343D48;
    font-size: 1.125rem;
  }

  h1 {
    font-weight: 600;
  }

  a {
    text-decoration: none;
    outline: none;
    color: #2E186A;

    :hover {
      color: #2e186a;
    }
  }

  *:focus {
    outline: none;
  }

  .ant-drawer-body {
    display: flex;
    flex-direction: column;
    text-align: left;
    padding: 2.5rem 2rem 1.25rem 1.25rem;
  }

  .ant-notification-notice-icon-success {
    color: rgb(255, 130, 92);
  }

  .fade-enter {
    opacity: 0;
    z-index: 1;
  }

  .fade-enter.fade-enter-active {
    opacity: 1;
    transition: opacity 250ms ease-in;
  }
  
  .ant-modal.custom-modal {
    &.mini {
      .ant-modal-body {
        padding: 15px !important;
      } 
    }
    
    &.user-info {
      .ant-modal-body {
        padding-top: 0px;
      } 
    }
    
    .ant-modal-header {
      border-radius: 10px;
      border-bottom: unset;
      padding-bottom: 0px;
    }
    
    .ant-modal-close-x{
      svg {
        fill: rgba(117, 117, 117, 0.72);
        width: 1em;
        height: 1em;
        display: inline-block;
        font-size: 1rem;
        flex-shrink: 0;
        user-select: none;
        margin-right: 5px;
      }
    }
    
    .ant-modal-content {
      padding: 10px;
      border-radius: 10px;
    }
    
    .ant-modal-body {
      padding-bottom: 0px;
    }
    
    .ant-modal-title {
      font-size: 18px;
    }
    
    .ant-modal-footer {
      border-top: unset;

      svg {
        fill: #fff;
        width: 1em;
        height: 1em;
        display: inline-block;
        font-size: 1rem;
        flex-shrink: 0;
        user-select: none;
        margin-right: 5px;
      }
    }
  }

  .ant-popover-inner {
    border-radius: 8px !important;
  }
  
  .ant-notification-close-x , .ant-picker{
    svg { 
      fill: #333;
    }
  }
  .ant-popover-inner-content {
    font-size: 12px !important;
    padding: 10px !important;
  }
  .ant-popover-title {
    min-width: unset !important;
    width: fit-content !important;
  }
  .ant-popconfirm {
    z-index: 1000;
  }
`

export default Styles
