import React from 'react'
import { Row, Col } from 'antd'
import SvgIcon from 'common/SvgIcon'
import * as S from './styles'
import { useTranslation } from 'react-i18next'
import { Bounce } from 'react-awesome-reveal'

const ProductBlock: React.FC = () => {
  const { t } = useTranslation()
  return (
    <S.ProductBlockContainer>
      <Row justify="space-between" align="middle" id={'product'}>
        <Col lg={11} md={11} sm={12} xs={24}>
          <Bounce triggerOnce duration={800}>
            <SvgIcon src={'waving.svg'} center />
          </Bounce>
        </Col>
        <Col lg={11} md={11} sm={11} xs={24}>
          <Bounce triggerOnce duration={800}>
            <S.ContentWrapper>
              <h6>{t('ProductBlock.Title')}</h6>
              <S.Content>{t('ProductBlock.Content')}</S.Content>
            </S.ContentWrapper>
          </Bounce>
        </Col>
      </Row>
    </S.ProductBlockContainer>
  )
}

export default ProductBlock
