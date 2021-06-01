import React, { useEffect } from 'react'
import { Menu } from 'antd'
import { DownOutlined, HomeTwoTone } from '@ant-design/icons'
import * as S from './styles'
import FixHeader from 'common/FixHeader'
import MemberList from 'common/MemberList'
import { useDispatch, useSelector } from 'react-redux'
import { Dispatch, RootState } from 'store'
import { FeedOrder } from 'models/feed'
import useRoom from 'hooks/useRoom'
import { Room } from 'models/room'
import useDocumentVisibility from 'hooks/useDocumentVisibility'
import useVirtualList from 'hooks/useVirtualList'
import EmptyStatus from 'common/EmptyStatus'
import { Helmet } from 'react-helmet'

const Feed: React.FC = () => {
  const feed = useSelector((state: RootState) => state.FeedModel)
  const $ = useDispatch<Dispatch>()
  const { enterRoom } = useRoom()
  const documentVisibility = useDocumentVisibility()
  const { list, containerProps, wrapperProps } = useVirtualList<Room>(feed.list, {
    overScan: 10,
    itemHeight: (i) => (feed.list[i]?.description ? 129 : 98)
  })

  useEffect(() => {
    if (documentVisibility !== 'visible') {
      return
    }
    (async () => {
      await $.FeedModel.setAsync()
    })()
  }, [documentVisibility])
  const menu = (
    <Menu>
      <Menu.ItemGroup title="房间创建时间">
        <S.MenuItem actived={feed.order === FeedOrder.CreatedTimeAsc}
                   onClick={async () => await $.FeedModel.setOrderAsync(FeedOrder.CreatedTimeAsc)}>升序</S.MenuItem>
        <S.MenuItem actived={feed.order === FeedOrder.CreatedTimeDesc}
                   onClick={async () => await $.FeedModel.setOrderAsync(FeedOrder.CreatedTimeDesc)}>倒序</S.MenuItem>
      </Menu.ItemGroup>
      <Menu.ItemGroup title="房间成员个数">
        <S.MenuItem actived={feed.order === FeedOrder.PeopleNumberAsc}
                   onClick={async () => await $.FeedModel.setOrderAsync(FeedOrder.PeopleNumberAsc)}>升序</S.MenuItem>
        <S.MenuItem actived={feed.order === FeedOrder.PeopleNumberDesc}
                   onClick={async () => await $.FeedModel.setOrderAsync(FeedOrder.PeopleNumberDesc)}>倒序</S.MenuItem>
      </Menu.ItemGroup>
    </Menu>
  )

  return (
    <S.Feed>
      <Helmet>
        <title>主页 / ITalking</title>
      </Helmet>
      <FixHeader title={'看看大家在聊什么'} icon={<HomeTwoTone />}>
        <S.DropdownMenu overlay={menu} trigger={['click']}>
          <a onClick={e => e.preventDefault()}>
            排序规则<DownOutlined/>
          </a>
        </S.DropdownMenu>
      </FixHeader>
      {feed.isEmpty
        ? (<EmptyStatus description="当前还没有人创建房间，快来试一下吧~"/>)
        : (
          <S.FeedListContainer {...containerProps}>
            <S.FeedList {...wrapperProps}>
              {list.map(({ data: feed }) =>
                <S.FeedItem key={feed.id} onClick={async () => await enterRoom(feed.id)}>
                  <S.FeedItemHeader>
                    <S.FeedName>{feed.name}</S.FeedName>
                    <S.FeedMembersCount><S.FeedDot/>{feed.speakers.length + feed.spectators.length}
                    </S.FeedMembersCount>
                  </S.FeedItemHeader>
                  {feed.description && <S.FeedDescription>{feed.description}</S.FeedDescription>}
                  <MemberList room={feed}/>
                </S.FeedItem>
              )}
            </S.FeedList>
          </S.FeedListContainer>
          )}
    </S.Feed>
  )
}

export default Feed
