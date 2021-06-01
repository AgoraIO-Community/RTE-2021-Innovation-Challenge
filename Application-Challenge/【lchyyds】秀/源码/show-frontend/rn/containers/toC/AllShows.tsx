import React, { useEffect, useState, useRef, useLayoutEffect, } from 'react';
import {
  View,
  Text,
  RefreshControl,
  FlatList,
  ActivityIndicator,
  Dimensions,
  ListRenderItemInfo,
} from 'react-native';
import { NavigationProp, } from '@react-navigation/core';
import { SearchBar, Header, } from 'react-native-elements';

import { searchShowService, ShowVO } from '../../services/show';
import ShowItem from '../../components/showlist/ShowItem';
import { TouchableWithoutFeedback } from 'react-native-gesture-handler';
const { width: screenWidth } = Dimensions.get('screen');
let pageSize = 12;
const debounce = (func: Function, ms: number) => {
  let timer: any = undefined;
  const res = (params: any) => {
    timer && clearTimeout(timer);
    timer = setTimeout(() => {
      func(params);
    }, ms);
  };
  return res;
};

interface AllShowsProps {
  navigation: NavigationProp<any>,
};

const AllShows: React.FC<AllShowsProps> = ({ navigation, }) => {
  const [showList, setShowList] = useState<ShowVO[] | undefined>(undefined);
  const [searchText, setSearchText] = useState('');
  const [refreshing, setRefreshing] = useState(false);

  const [currentPage, setCurrentPage] = useState(0);
  const [hasNext, setHasNext] = useState(true);
  const [loading, setLoading] = useState(false);
  const [canLoadMore, setCanLoadMore] = useState(false);
  const [pageSizeInited, setPageSizeInited] = useState(false);

  const searchBarRef = useRef<any>(null);

  useEffect(() => {
    console.log('~~~~~~~~~~~~~~ did mount')
  }, [])

  useEffect(() => {
    if (searchBarRef.current) {
      searchBarRef.current.debounceSearch = debounce(_search, 200);
    }
  }, [searchBarRef.current]);

  const initSearchControl = () => {
    setCurrentPage(0);
    setHasNext(true);
  };

  const _search = (keyword: string, pageNumber: number = 0) => {
    const trimed = keyword.trim();
    return (
      searchShowService({ keyword: trimed, pageNumber, pageSize })
        .then((res) => {
          if (res.success) {
            setHasNext(!!res.data?.hasNext);
            const newList = res.data?.showVOList || [];
            setShowList(pageNumber === 0 ? newList : [...(showList || []), ...newList]);
          }
          return res;
        })
    );
  };

  const handleChangeSearchText = (text: string) => {
    initSearchControl();

    setSearchText(text);
    searchBarRef.current.debounceSearch(text, 0);
  };

  const handleRefresh = () => {
    initSearchControl();

    setRefreshing(true);
    _search(searchText, 0)
      .finally(() => {
        setRefreshing(false);
      });
  };

  const handleEndReached = () => {
    if (!canLoadMore) { return; }
    if (!hasNext || loading) { return; }

    setLoading(true);
    setCanLoadMore(false);
    _search(searchText, currentPage + 1)
      .then((res) => {
        if (res.success) {
          setCurrentPage(currentPage + 1);
        }
      })
      .finally(() => {
        setLoading(false);
      });
  };

  const renderShowItem = (show: ShowVO) => {
    return (
      <ShowItem key={show.showId} navigation={navigation} show={show}></ShowItem>
    );
  };

  const renderFlatListItem = ({ item, index, separators }: ListRenderItemInfo<any>) => (
    <View key={item.showId} style={{ width: screenWidth / 2 - 2, marginLeft: index % 2 ? 4 : 0 }}>
      {renderShowItem(item)}
    </View>
  );

  const renderListFooterComponent = () => (
    loading
      ? (
        <View style={{ flexDirection: 'row', justifyContent: 'center', paddingTop: 15, paddingBottom: 15, }}>
          <ActivityIndicator size='small' color="rgb(166, 176, 184)" />
          <Text style={{ fontSize: 16, marginLeft: 10, color: 'rgb(166, 176, 184)' }}>加载更多</Text>
        </View>
      )
      : null
  );

  const renderShowList = () => {
    if (showList === undefined) {
      return (
        <View style={{ flexDirection: 'row', justifyContent: 'center', margin: 50, }}>
          <ActivityIndicator size='small' color="rgb(166, 176, 184)" />
          <Text style={{ fontSize: 16, marginLeft: 10, color: 'rgb(166, 176, 184)' }}>加载中...</Text>
        </View>
      );
    }

    if (!showList?.length) {
      return (
        <View style={{ margin: 50, alignItems: 'center' }}>
          <Text style={{ textAlign: 'center', fontSize: 20, color: 'tomato' }}>
            😅暂无符合条件的演出~
          </Text>
          <TouchableWithoutFeedback
            onPress={() => {
              searchBarRef.current.debounceSearch(searchText, currentPage);
            }}
          >
            <Text style={{ marginTop: 10, fontSize: 12, color: 'blue', textDecorationLine: 'underline' }}>刷新试试</Text>
          </TouchableWithoutFeedback>
        </View>
      );
    }

    return (
      <FlatList
        style={{ flex: 1 }}
        numColumns={2}
        data={showList}
        keyExtractor={(item: ShowVO, index: number) => item.showId}
        renderItem={renderFlatListItem}
        refreshControl={<RefreshControl refreshing={refreshing} onRefresh={handleRefresh} />}
        ListFooterComponent={renderListFooterComponent}
        onEndReached={handleEndReached}
        onContentSizeChange={() => setCanLoadMore(true)}
        onEndReachedThreshold={0.01}
      >
      </FlatList>
    );
  };

  return (
    <View
      style={{
        backgroundColor: '#ffffff',
        flex: 1,
      }}
    >
      <Header
        backgroundColor='#f1f1f1'
        containerStyle={{ height: 0 }}
      />
      <SearchBar
        ref={searchBarRef}
        platform='android'
        placeholder='搜索演出'
        onChangeText={handleChangeSearchText}
      />
      <View
        style={{
          backgroundColor: '#f1f1f1',
          flex: 1,
        }}
        onLayout={(event) => {
          if (!pageSizeInited) {
            const { height: viewHeight } = event.nativeEvent.layout;
            pageSize = Math.ceil(viewHeight / 138) * 2;
            setPageSizeInited(true);
            _search(searchText, currentPage);
          }
        }}
      >
        {renderShowList()}
      </View>
    </View>
  );
};

export default AllShows;