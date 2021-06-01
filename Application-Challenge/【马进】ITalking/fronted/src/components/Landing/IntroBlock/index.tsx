import React from 'react'
import { Row, Col } from 'antd'
import SvgIcon from 'common/SvgIcon'
import Button from 'common/Landing/Button'
import * as S from './styles'
import { useTranslation } from 'react-i18next'
import { Slide } from 'react-awesome-reveal'
import { scrollTo } from 'helpers/browser'
import { useHistory } from 'react-router-dom'
import RouteMap from 'types/route'

const IntroBlock: React.FC = () => {
  const { t } = useTranslation()
  const history = useHistory()
  return (
    <S.IntroBlockContainer id="intro">
      <Row justify="space-between" align="middle">
        <Col lg={11} md={11} sm={11} xs={24}>
          <Slide direction={'left'} triggerOnce duration={800}>
            <S.ContentWrapper>
              <h6>{t('IntroBlock.Title')}</h6>
              <S.Content>{t('IntroBlock.Content')}</S.Content>
              <S.ButtonWrapper>
                <Button
                  primary={false}
                  onClick={() => scrollTo('about')}
                >
                  {t('IntroBlock.LearnButton')}
                </Button>
                <Button onClick={() => history.push(RouteMap.Entry) } >
                  {t('IntroBlock.ExploreButton')}
                </Button>
              </S.ButtonWrapper>
            </S.ContentWrapper>
          </Slide>
        </Col>
        <Col lg={11} md={11} sm={12} xs={24}>
          <Slide direction={'right'} triggerOnce duration={800}>
            <SvgIcon src={'developer.svg'} center />
          </Slide>
        </Col>
      </Row>
    </S.IntroBlockContainer>
  )
}

export default IntroBlock
