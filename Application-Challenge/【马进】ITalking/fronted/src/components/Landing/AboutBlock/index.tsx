import React from 'react'
import { Row, Col } from 'antd'
import SvgIcon from 'common/SvgIcon'
import * as S from './styles'
import { useTranslation } from 'react-i18next'
import { JackInTheBox } from 'react-awesome-reveal'

const AboutBlock: React.FC = () => {
  const { t } = useTranslation()
  return (
    <S.AboutBlock>
      <Row justify="space-between" align="middle" id={'about'}>
        <Col lg={11} md={11} sm={12} xs={24}>
          <JackInTheBox triggerOnce duration={800}>
            <SvgIcon src={'graphs.svg'} center />
          </JackInTheBox>
        </Col>
        <Col lg={11} md={11} sm={11} xs={24}>
          <JackInTheBox triggerOnce duration={800}>
            <S.ContentWrapper>
              <h6>{t('AboutBlock.Title')}</h6>
              <S.Content>{t('AboutBlock.Content')}</S.Content>
              <S.ServiceWrapper>
                <Row justify="space-between">
                  <Col span={11}>
                    <SvgIcon src={'notes.svg'} width={60} height={60}/>
                    <S.MinTitle>{t('AboutBlock.FirstSelection.Title')}</S.MinTitle>
                    <S.MinPara>{t('AboutBlock.FirstSelection.Content')}</S.MinPara>
                  </Col>
                  <Col span={11}>
                    <SvgIcon src={'notes.svg'} width={60} height={60}/>
                    <S.MinTitle>{t('AboutBlock.SecondSelection.Title')}</S.MinTitle>
                    <S.MinPara>{t('AboutBlock.SecondSelection.Content')}</S.MinPara>
                  </Col>
                </Row>
              </S.ServiceWrapper>
            </S.ContentWrapper>
          </JackInTheBox>
        </Col>
      </Row>
    </S.AboutBlock>
  )
}

export default AboutBlock
