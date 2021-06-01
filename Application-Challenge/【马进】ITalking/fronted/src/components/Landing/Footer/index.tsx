import React from 'react'
import { Row, Col } from 'antd'
import i18n from 'i18next'
import { useTranslation, withTranslation } from 'react-i18next'
import { Slide } from 'react-awesome-reveal'
import * as S from './styles'
import Container from 'common/Landing/Container'
import SvgIcon from 'common/SvgIcon'
import { scrollTo } from 'helpers/browser'
import App from 'constants/app'

interface Props {
  href: string
  src: string
}

const SocialLink: React.FC<Props> = props => {
  const { href, src } = props
  return (
    <S.SocialLink
      href={href}
      target="_blank"
      rel="noopener noreferrer"
      key={src}
      aria-label={src}
    >
      <SvgIcon src={src} width={25} height={25}/>
    </S.SocialLink>
  )
}

const Footer: React.FC = () => {
  const { t } = useTranslation()
  const changeLanguage = (event: any) => {
    i18n.changeLanguage(event.target.value)
  }
  return (
    <>
      <Slide direction={'up'} triggerOnce duration={800}>
        <S.Footer>
          <Container>
            <Row justify="space-between">
              <Col lg={10} md={10} sm={12} xs={24}>
                <S.Language>{t('Footer.Contact')}</S.Language>
                <S.Large href={App.AuthorUrl} target="_blank">{t('Footer.Tell us everything')}</S.Large>
                <S.Para>
                  {t('Footer.Question')}
                </S.Para>
                <a href="mailto:simon@tomotoes.com">
                  <S.Chat>{t('Footer.Let\'s Chat')}</S.Chat>
                </a>
              </Col>
              <Col lg={8} md={8} sm={12} xs={24}>
                <S.Title>{t('Footer.Cooperation')}</S.Title>
                <S.Large href="mailto:simon@tomotoes.com" left>
                  {t('Footer.Business Cooperation')}
                </S.Large>
              </Col>
              <Col lg={6} md={6} sm={12} xs={24}>
                <S.Title>{t('Footer.Help')}</S.Title>
                <S.Large left href={App.IssuesUrl} target="_blank">
                  {t('Footer.Safe Feedback')}
                </S.Large>
              </Col>
            </Row>
            <Row justify="space-between">
              <Col lg={10} md={10} sm={12} xs={24}>
                <S.Empty/>
                <S.Language>{t('Footer.Address.Label')}</S.Language>
                <S.Para>{t('Footer.Address.Country')}</S.Para>
                <S.Para>{t('Footer.Address.Province')}</S.Para>
                <S.Para>{t('Footer.Address.City')}</S.Para>
              </Col>
              <Col lg={8} md={8} sm={12} xs={24}>
                <S.Title>{t('Footer.Navigation')}</S.Title>
                <S.Large left onClick={() => scrollTo('about')}>
                  {t('Header.About')}
                </S.Large>
                <S.Large left onClick={() => scrollTo('mission')}>
                  {t('Header.Mission')}
                </S.Large>
                <S.Large left onClick={() => scrollTo('product')}>
                  {t('Header.Product')}
                </S.Large>
              </Col>
              <Col lg={6} md={6} sm={12} xs={24}>
                <S.Select>
                  <S.Label htmlFor="select-lang">{t('Footer.Language')}</S.Label>
                  <S.LangSelect
                    onChange={changeLanguage}
                    value={i18n.language}
                    id="select-lang"
                  >
                    <option value="en">English</option>
                    <option value="zh">Chinese</option>
                  </S.LangSelect>
                </S.Select>
              </Col>
            </Row>
          </Container>
        </S.Footer>
      </Slide>
      <Slide direction={'down'} triggerOnce duration={800}>
        <S.Extra>
          <S.BorderContainer>
            <Row
              justify="space-between"
              align="middle"
              style={{ paddingTop: '3rem' }}
            >
              <S.NavLink to="/">
                <S.LogoContainer>
                  <SvgIcon
                    src="logo.svg"
                    aria-label="homepage"
                    width={101}
                    height={64}
                  />
                </S.LogoContainer>
              </S.NavLink>
              <S.FooterContainer>
                <SocialLink
                  href="https://github.com/Tomotoes/ITalking"
                  src="github.svg"
                />
                <SocialLink
                  href="https://twitter.com/simon_aking"
                  src="twitter.svg"
                />
                <SocialLink
                  href="https://tomotoes.com"
                  src="medium.svg"
                />
                <SocialLink
                  href="https://www.instagram.com/simonaking72/"
                  src="instagram.svg"
                />
              </S.FooterContainer>
            </Row>
          </S.BorderContainer>
        </S.Extra>
      </Slide>
    </>
  )
}

export default withTranslation()(Footer)
