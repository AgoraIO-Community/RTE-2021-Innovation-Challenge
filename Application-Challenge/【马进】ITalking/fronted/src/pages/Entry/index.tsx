import React, { useState } from 'react'

import useWindowWidth from 'hooks/useWindowWidth'
import { useTranslation } from 'react-i18next'

import { Fade } from 'react-awesome-reveal'
import * as S from './styles'
import EntryApi from 'services/entry'
import { isFailedResponse } from 'helpers/http'
import { LoginResponse, SignInResponse } from 'types/entry'
import { message } from 'antd'
import RouteMap from 'types/route'
import App from 'constants/app'
import { Helmet } from 'react-helmet'

const Entry: React.FC = () => {
  const windowWidth = useWindowWidth()
  const isMobileScreen = windowWidth < 900

  const { t } = useTranslation()
  const [name, setName] = useState('')
  const [nameError, setNameError] = useState('')
  const [password, setPassword] = useState('')
  const [passwordError, setPasswordError] = useState('')
  const [confirmedPassword, setConfirmedPassword] = useState('')
  const [isSignIn, setIsSignIn] = useState(true)
  const [isLoading, setIsLoading] = useState(false)

  const changeName = (e: React.FormEvent<HTMLInputElement>) => {
    setName(e.currentTarget.value)
    setNameError('')
  }
  const changePassword = (e: React.FormEvent<HTMLInputElement>) => {
    setPassword(e.currentTarget.value)
    setPasswordError('')
  }
  const changeConfirmedPassword = (e: React.FormEvent<HTMLInputElement>) => {
    setConfirmedPassword(e.currentTarget.value)
    setPasswordError('')
  }
  const changeForm = () => {
    setIsSignIn(!isSignIn)
    setName('')
    setConfirmedPassword('')
    setPassword('')
    setNameError('')
    setPasswordError('')
  }
  const checkFormError = () => {
    let existNameError = true
    let existPasswordError = true
    if (!name?.trim()) {
      setNameError('请填写昵称')
    } else if (!/^[\u4e00-\u9fa5_a-zA-Z0-9\s]+$/.test(name)) {
      setNameError('暂不支持包含特殊字符的昵称')
    } else if (name.length > App.NameMaxLength) {
      setNameError(`暂只支持${App.NameMaxLength}个字符以内的昵称`)
    } else if (name.length < App.NameMinLength) {
      setNameError(`暂只支持${App.NameMinLength}个字符以上的昵称`)
    } else {
      existNameError = false
    }
    if (!password) {
      setPasswordError('请填写密码')
    } else if (!/^[a-zA-Z0-9._!@#]+$/.test(password)) {
      setPasswordError('暂不支持包含特殊字符的密码')
    } else if (password.length > App.PasswordMaxLength) {
      setPasswordError(`暂只支持${App.PasswordMaxLength}个字符以内的密码`)
    } else if (password.length < App.PasswordMinLength) {
      setPasswordError(`暂只支持${App.PasswordMinLength}个字符以上的密码`)
    } else if (isSignIn && !confirmedPassword) {
      setPasswordError('请确认一遍密码')
    } else if (isSignIn && confirmedPassword !== password) {
      setPasswordError('两次输入的密码不一致')
    } else {
      existPasswordError = false
    }
    return existPasswordError || existNameError
  }
  const signIn = async () => {
    if (isLoading) {
      return
    }
    const existError = checkFormError()
    if (existError) {
      return
    }
    setIsLoading(true)
    const response = await EntryApi.SignIn(name, password)
    setIsLoading(false)
    if (isFailedResponse(response)) {
      const { type } = response.data
      if (type === SignInResponse.NameConflict) {
        message.warn('该名字已被占用，请换一个吧')
      }
      return
    }
    location.pathname = RouteMap.Feed
  }
  const login = async () => {
    if (isLoading) {
      return
    }
    const existError = checkFormError()
    if (existError) {
      return
    }
    setIsLoading(true)
    const response = await EntryApi.Login(name, password)
    setIsLoading(false)
    if (isFailedResponse(response)) {
      const { type } = response.data
      if (type === LoginResponse.NonExistent) {
        message.warn('您输入的账号不存在')
      }
      if (type === LoginResponse.PasswordError) {
        message.warn('您输入的密码不正确')
      }
      return
    }
    location.pathname = RouteMap.Feed
  }
  const LoginForm = (
    <S.Form>
      <S.Title>{t('Login.Title')}</S.Title>
      <S.Input type="text" placeholder={t('Login.Name')} value={name} onChange={changeName}/>
      <S.ErrorTip show={!!nameError}>{nameError}</S.ErrorTip>
      <S.Input type="password" placeholder={t('Login.Password')} value={password}
               onChange={changePassword}/>
      <S.ErrorTip show={!!passwordError}>{passwordError}</S.ErrorTip>
      <S.Button onClick={login}>{t('Login.Button')}</S.Button>
    </S.Form>

  )
  const SignInForm = (
    <S.Form>
      <S.Title>{t('SignIn.Title')}</S.Title>
      <S.Input type="text" placeholder={t('SignIn.Name')} value={name} onChange={changeName}/>
      <S.ErrorTip show={!!nameError}>{nameError}</S.ErrorTip>
      <S.Input type="password" placeholder={t('SignIn.Password')} value={password}
               onChange={changePassword}/>
      <S.Input type="password" placeholder={t('SignIn.Confirm')} value={confirmedPassword}
               onChange={changeConfirmedPassword}/>
      <S.ErrorTip show={!!passwordError}>{passwordError}</S.ErrorTip>
      <S.Button onClick={signIn}>{t('SignIn.Button')}</S.Button>
    </S.Form>
  )

  const PCContainer = (
    <S.PCForm>
      <S.SignInContainer isSignIn={isSignIn}>
        {SignInForm}
      </S.SignInContainer>
      <S.LoginContainer isSignIn={isSignIn}>
        {LoginForm}
      </S.LoginContainer>
      <S.PCSwitch isSignIn={isSignIn}>
        <S.PCBottomCircle isSignIn={isSignIn}/>
        <S.PCTopCircle isSignIn={isSignIn}/>
        <S.PCSwitchContainer isSignIn={isSignIn}>
          <S.Title>{t('SignIn.Hi')}</S.Title>
          <S.Description>{t('SignIn.Description')}</S.Description>
          <S.FormLink to="/">{t('SignIn.Back')}</S.FormLink>
          <S.PCSwitchButton onClick={changeForm}>{t('SignIn.Toggle')}</S.PCSwitchButton>
        </S.PCSwitchContainer>
        <S.PCSwitchContainer isSignIn={!isSignIn}>
          <S.Title>{t('Login.Hi')}</S.Title>
          <S.Description>{t('Login.Description')}</S.Description>
          <S.FormLink to="/">{t('Login.Back')}</S.FormLink>
          <S.PCSwitchButton onClick={changeForm}>{t('Login.Toggle')}</S.PCSwitchButton>
        </S.PCSwitchContainer>
      </S.PCSwitch>
    </S.PCForm>
  )

  const MobileContainer = (
    <S.MobileForm>
      <S.MobileContainer>
        <Fade duration={800}>
          {isSignIn ? SignInForm : LoginForm}
        </Fade>
      </S.MobileContainer>
      <S.MobileSwitch>
        <S.MobileDescription>
          {t(isSignIn ? 'SignIn.Description' : 'Login.Description')}
          <S.MobileButton onClick={changeForm}>{t(isSignIn ? 'SignIn.Toggle' : 'Login.Toggle')}</S.MobileButton>
        </S.MobileDescription>
      </S.MobileSwitch>
    </S.MobileForm>
  )

  return (
    <Fade triggerOnce duration={800}>
      <Helmet>
        <title>入口 / ITalking</title>
      </Helmet>
      <S.Container>
        {isMobileScreen ? MobileContainer : PCContainer}
      </S.Container>
    </Fade>
  )
}

export default Entry
