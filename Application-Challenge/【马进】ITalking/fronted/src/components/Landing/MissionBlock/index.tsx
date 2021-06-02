import React from 'react'
import { Row, Col } from 'antd'
import SvgIcon from 'common/SvgIcon'
import * as S from './styles'
import { useTranslation } from 'react-i18next'
import { Zoom } from 'react-awesome-reveal'

const MissionBlock: React.FC = () => {
  const { t } = useTranslation()
  return (
    <S.MissionBlockContainer id="mission">
      <Row justify="space-between" align="middle">
        <Col lg={11} md={11} sm={11} xs={24}>
          <Zoom triggerOnce duration={800}>
            <S.ContentWrapper>
              <h6>{t('MissionBlock.Title')}</h6>
              <S.Content>{t('MissionBlock.Content')}</S.Content>
            </S.ContentWrapper>
          </Zoom>
        </Col>
        <Col lg={11} md={11} sm={12} xs={24}>
          <Zoom triggerOnce duration={800}>
            <SvgIcon src={'product-launch.svg'} center />
          </Zoom>
        </Col>
      </Row>
    </S.MissionBlockContainer>
  )
}

export default MissionBlock
