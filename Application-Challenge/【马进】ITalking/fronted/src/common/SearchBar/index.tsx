import React, { useEffect, useState } from 'react'
import * as S from './styles'
import { SearchOutlined } from '@ant-design/icons'
import useDebounce from 'hooks/useDebounce'
import { Empty, Spin, Tag } from 'antd'
import { Fade } from 'react-awesome-reveal'
import { Tip } from 'types/search'
import EntryApi from 'services/entry'
import { isFailedResponse } from 'helpers/http'
import { User } from 'models/user'
import Avatar from 'common/Avatar'
import useUserCard from 'hooks/useUserCard'
import useRoom from 'hooks/useRoom'

const SearchBar: React.FC = () => {
  const userCard = useUserCard()
  const { enterRoom } = useRoom()

  const [_searchText, setSearchText] = useState<string>('')
  const searchText = useDebounce(_searchText, {
    filter: (v: string) => v.trim()
  })
  const [searchHash, setSearchHash] = useState<{
    [searchText: string]: Tip
  }>({})
  const [tipListVisible, setTipListVisible] = useState<boolean>(false)
  const [loading, setLoading] = useState<boolean>(false)

  const addTipList = (text: string, tipList: Tip) => {
    if (searchHash[text]) {
      return
    }
    setSearchHash(prevState => ({
      ...prevState,
      [text]: tipList
    }))
  }

  const handleClickAway = (event: any) => {
    const tipListRef = document.getElementById('tip-list')
    if (!tipListRef || tipListRef?.contains(event.target)) {
      return
    }
    setTipListVisible(false)
  }

  useEffect(() => {
    document.addEventListener('click', handleClickAway)
    return () => {
      document.removeEventListener('click', handleClickAway)
    }
  }, [])

  useEffect(() => {
    (async () => {
      if (!searchText.trim()) {
        return
      }
      if (searchHash[searchText]) {
        setLoading(false)
        return
      }
      const response = await EntryApi.Search(searchText)
      if (isFailedResponse(response)) {
        return
      }
      addTipList(searchText, response.data.data)
      setLoading(false)
    })()
  }, [searchText])

  const changeSearchText = (e: React.FormEvent<HTMLInputElement>) => {
    const newSearchText = e.currentTarget.value
    setSearchText(newSearchText)
    setLoading(true)
    setTipListVisible(!!newSearchText)
  }
  const viewUser = async (user: User) => {
    setSearchText('')
    setTipListVisible(false)
    await userCard.view(user.id)
  }

  return (
    <S.Search>
      <S.SearchBar>
        <S.SearchInput value={_searchText} onChange={changeSearchText} size="large" placeholder="快速搜索房间，用户"
                       prefix={<SearchOutlined/>}
                       bordered={false}/>
      </S.SearchBar>
      <S.TipPanel visible={tipListVisible} id="tip-list">
        {(() => {
          if (!tipListVisible) {
            return
          }
          const tip = searchHash[searchText]
          if (loading) {
            return <S.SearchStatus><Spin size="large" tip="正在搜索中..."/></S.SearchStatus>
          }
          if (!tip?.rooms?.length && !tip?.users?.length) {
            return <S.SearchStatus><Empty image={Empty.PRESENTED_IMAGE_SIMPLE}
                                          description="未能找到相应的信息"/></S.SearchStatus>
          }
          const { rooms, users } = tip
          return (
            <Fade duration={800}>
              <S.TipList>
                {users?.map(user => (
                  <S.TipItem key={user.id} onClick={async () => await viewUser(user)}>
                    <S.TipType>
                      <Tag color="blue">用户</Tag>
                    </S.TipType>
                    <S.TipName>
                      <S.TipAvatar><Avatar name={user.name} size={40}/></S.TipAvatar>
                      {user.name}
                    </S.TipName>
                  </S.TipItem>
                ))}
                {rooms?.map(room => (
                  <S.TipItem key={room.id} onClick={async () => await enterRoom(room.id)}>
                    <S.TipType>
                      <Tag color="magenta">房间</Tag>
                    </S.TipType>
                    <S.TipName>
                      {room.name}
                    </S.TipName>
                  </S.TipItem>
                ))}
              </S.TipList>
            </Fade>
          )
        })()}
      </S.TipPanel>
    </S.Search>

  )
}

export default SearchBar
