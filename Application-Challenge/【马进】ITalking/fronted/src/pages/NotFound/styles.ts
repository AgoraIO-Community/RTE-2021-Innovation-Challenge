import styled, { createGlobalStyle } from 'styled-components'

export const NotFoundAnimations = createGlobalStyle`
  .fundo {
    animation: scales 3s alternate infinite;
    transform-origin: center;
  }

  .pao-baixo {
    animation: rotatepao 14s cubic-bezier(.1, .49, .41, .97) infinite;
    transform-origin: center;
  }

  .pao-cima {
    animation: rotatepao 7s 1s cubic-bezier(.1, .49, .41, .97) infinite;
    transform-origin: center;
  }

  .olhos {
    animation: olhos 2s alternate infinite;
    transform-origin: center;
  }

  .left-sparks {
    animation: left-sparks 4s alternate infinite;
    transform-origin: 150px 156px;
  }

  .right-sparks {
    animation: left-sparks 4s alternate infinite;
    transform-origin: 310px 150px;
  }

  .olhos {
    animation: olhos 2s alternate infinite;
    transform-origin: center;
  }

  @keyframes scales {
    from {
      transform: scale(0.98)
    }
    to {
      transform: scale(1)
    }
  }

  @keyframes rotatepao {
    0% {
      transform: rotate(0deg)
    }
    50%, 60% {
      transform: rotate(-20deg)
    }
    100% {
      transform: rotate(0deg)
    }

  }

  @keyframes olhos {
    0% {
      transform: rotateX(0deg);
    }
    100% {
      transform: rotateX(30deg);
    }
  }

  @keyframes left-sparks {
    0% {
      opacity: 0;
    }

  }

  @keyframes dash {
    0%, 30% {
      fill: #4B4B62;
      stroke-dashoffset: 0;
    }
    80%, 100% {
      fill: transparent;
      stroke-dashoffset: -200;
    }
  }
`

export const NotFound = styled.div`
  width: 100%;
  height: 100vh;
  padding: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  transform: translateY(-8%);
`

export const NotFoundTip = styled.div`
  margin-top: 30px;
  margin-bottom: 20px;

  * {
    text-align: center;
  }
`

export const NotFoundBtn = styled.div`
  --blue: #0e0620;
  --white: #fff;
  --primary: #2a1c66;
  
  z-index: 1;
  overflow: hidden;
  background: transparent;
  position: relative;
  padding: 8px 50px;
  border-radius: 30px;
  cursor: pointer;
  font-size: 1em;
  letter-spacing: 2px;
  transition: 0.2s ease;
  font-weight: bold;
  border: 4px solid var(--primary);
  color: var(--blue);

  &:before {
    content: "";
    position: absolute;
    left: 0;
    top: 0;
    width: 0%;
    height: 100%;
    background: var(--primary);
    z-index: -1;
    transition: 0.2s ease;
  }

  &:hover {
    color: var(--white);
    background: var(--primary);
    transition: 0.2s ease;

    &:before {
      width: 100%;
    }
  }
`

export const NotFoundPath = styled.path`
  stroke-dasharray: 300;
  stroke-dashoffset: 300;
  animation: dash 4s alternate infinite;
`
