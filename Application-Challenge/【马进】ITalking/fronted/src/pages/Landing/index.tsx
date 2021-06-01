import React from 'react'
import Header from 'components/Landing/Header'
import Container from 'common/Landing/Container'
import IntroBlock from 'components/Landing/IntroBlock'
import IntroMiddleBlock from 'components/Landing/IntroMiddleBlock'
import AboutBlock from 'components/Landing/AboutBlock'
import MissionBlock from 'components/Landing/MissionBlock'
import ProductBlock from 'components/Landing/ProductBlock'
import Footer from 'components/Landing/Footer'
import { Helmet } from 'react-helmet'

const Landing: React.FC = () => {
  return (
    <>
      <Helmet>
        <title>ITalking - 打破交流的屏障</title>
      </Helmet>
      <Header/>
      <Container>
        <IntroBlock/>
        <IntroMiddleBlock/>
        <AboutBlock/>
        <MissionBlock/>
        <ProductBlock/>
      </Container>
      <Footer/>
    </>
  )
}

export default Landing
