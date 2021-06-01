import React, { useState } from 'react'
import { Row, Col, Drawer } from 'antd'
import { CSSTransition } from 'react-transition-group'
import { useTranslation } from 'react-i18next'
import * as S from './styles'
import SvgIcon from 'common/SvgIcon'
import Button from 'common/Landing/Button'
import { Fade } from 'react-awesome-reveal'
import { scrollTo } from 'helpers/browser'
import { useHistory } from 'react-router-dom'
import RouteMap from 'types/route'

const Header: React.FC = () => {
  const { t } = useTranslation()
  const [drawerVisible, setDrawerVisible] = useState(false)
  const history = useHistory()
  const showDrawer = () => {
    setDrawerVisible(true)
  }

  const closeDrawer = () => {
    setDrawerVisible(false)
  }

  const MenuItem = () => {
    return (
      <>
        <S.CustomNavLinkSmall onClick={() => scrollTo('about', () => setDrawerVisible(false)
        )}>
          <S.Span>{t('Header.About')}</S.Span>
        </S.CustomNavLinkSmall>
        <S.CustomNavLinkSmall onClick={() => scrollTo('mission', () => setDrawerVisible(false)
        )}>
          <S.Span>{t('Header.Mission')}</S.Span>
        </S.CustomNavLinkSmall>
        <S.CustomNavLinkSmall onClick={() => scrollTo('product', () => setDrawerVisible(false)
        )}>
          <S.Span>{t('Header.Product')}</S.Span>
        </S.CustomNavLinkSmall>
        <S.CustomNavLinkSmall
          style={{ width: '180px' }}
          onClick={() => scrollTo('contact', () => setDrawerVisible(false))}
        >
          <S.Span>
            <Button onClick={() => history.push(RouteMap.Entry)}>{t('Header.Quick-Start')}</Button>
          </S.Span>
        </S.CustomNavLinkSmall>
      </>
    )
  }

  return (
    <S.Header>
      <S.Container>
        <Fade triggerOnce duration={800}>
          <Row justify="space-between" gutter={20}>
            <S.LogoContainer to="/" aria-label="homepage">
              <SvgIcon src="logo.svg" width={100} height={64}/>
            </S.LogoContainer>
            <S.NotHidden>
              <MenuItem/>
            </S.NotHidden>
            <S.Burger onClick={showDrawer}>
              <S.Outline/>
            </S.Burger>
          </Row>
        </Fade>
        <CSSTransition in timeout={350} unmountOnExit >
          <Drawer closable={false} visible={drawerVisible} onClose={closeDrawer}>
            <Col style={{ marginBottom: '2.5rem' }}>
              <S.Label onClick={closeDrawer}>
                <Col span={12}>
                  <S.Menu>{t('Header.Menu')}</S.Menu>
                </Col>
                <Col span={12}>
                  <S.Outline padding/>
                </Col>
              </S.Label>
            </Col>
            <MenuItem/>
          </Drawer>
        </CSSTransition>
      </S.Container>
    </S.Header>
  )
}

export default Header
