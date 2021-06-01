import React from 'react'
import { Row, Col } from 'antd'
import * as S from './styles'
import { useTranslation } from 'react-i18next'
import Button from 'common/Landing/Button'
import { Slide } from 'react-awesome-reveal'
import { scrollTo } from 'helpers/browser'

const IntroMiddleBlock: React.FC = () => {
  const { t } = useTranslation()
  return (
    <S.IntroMiddleBlock id="intro-middle">
      <Row justify="center" align="middle">
        <Slide direction={'up'} triggerOnce duration={800}>
          <S.ContentWrapper>
            <Col lg={24} md={24} sm={24} xs={24}>
              <h6>{t('IntroMiddleBlock.Title')}</h6>
              <S.Content>{t('IntroMiddleBlock.Content')}</S.Content>
              <Button onClick={() => scrollTo('mission')}>
                {t('IntroMiddleBlock.Button')}
              </Button>
            </Col>
          </S.ContentWrapper>
        </Slide>
      </Row>
    </S.IntroMiddleBlock>
  )
}

export default IntroMiddleBlock
