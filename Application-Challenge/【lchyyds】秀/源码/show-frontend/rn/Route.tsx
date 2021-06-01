import React, { Dispatch, useState, } from 'react';
import 'react-native-gesture-handler';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator, StackHeaderProps } from '@react-navigation/stack';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { Icon, } from 'react-native-elements'
const Tab = createBottomTabNavigator();

import Mine from './containers/common/Mine';
import AllShows from './containers/toC/AllShows';
import PurchasedShows from './containers/toC/PurchasedShows';
import ShowChannel from './containers/common/ShowChannel';
import UpsertShow from './containers/toB/UpsertShow';
import { Role, UserVO } from './services/user';
import { TabNav, HomeStackNav, } from './constants/Nav';
import LoginHOC from './enhancers/LoginHOC';
import { TabNavHeight } from './constants/Layout';
const ShowStack = createStackNavigator();

interface RouteProps {
  user: UserVO,
  userDispatch: Dispatch<any>,
};

const Route: React.FC<RouteProps> = ({ user, userDispatch }) => {
  const [hideTabBar, setHideTabBar] = useState(false);

  const handleChangeTabBar = (visible: boolean) => {
    setHideTabBar(!visible);
  };

  const tabScreenOptions = ({ route }: any) => ({
    tabBarIcon: ({ focused, color, size }: any) => {
      const iconColor = focused ? 'tomato' : 'gray';
      const RouteIconMap: { [k: string]: any } = {
        [TabNav.Home]: <Icon type='font-awesome' name='tv' color={iconColor} />,
        [TabNav.Purchased]: <Icon type='font-awesome' name='heart-o' color={iconColor} />,
        [TabNav.Publish]: <Icon type='font-awesome' name='bullhorn' color={iconColor} />,
        [TabNav.Mine]: <Icon type='font-awesome' name='user-o' color={iconColor} />,
      };
      // TODO: our tiny logo
      const DefaultIcon = <Icon type='font-awesome' name='star-o' color={iconColor} />;

      return RouteIconMap[route.name] || DefaultIcon;
    },
  });

  return (
    <NavigationContainer>
      <Tab.Navigator
        initialRouteName={TabNav.Home}
        screenOptions={tabScreenOptions}
        tabBarOptions={{
          activeTintColor: 'tomato',
          inactiveTintColor: 'gray',
          style: { height: TabNavHeight },
        }}
        {...(hideTabBar ? { tabBar: () => null } : {})}
      >
        {/* 首页 */}
        <Tab.Screen name={TabNav.Home}>
          {() => (
            <ShowStack.Navigator initialRouteName={HomeStackNav.AllShows}>
              {/* 全部演出 */}
              <ShowStack.Screen name={HomeStackNav.AllShows} options={{ header: (props: StackHeaderProps) => null }}>
                {(props) => <AllShows {...props} user={user} userDispatch={userDispatch}></AllShows>}
              </ShowStack.Screen>
              {/* 演出频道 */}
              <ShowStack.Screen name={HomeStackNav.ShowChannel} options={{ header: (props: StackHeaderProps) => null }}>
                {(props) => <ShowChannel {...props} user={user} onChangeTabBar={handleChangeTabBar}></ShowChannel>}
              </ShowStack.Screen>
            </ShowStack.Navigator>
          )}
        </Tab.Screen>
        {/* 已购 */}
        <Tab.Screen name={TabNav.Purchased}>
          {(props) => <PurchasedShows {...props} user={user} ></PurchasedShows>}
        </Tab.Screen>
        {/* 发布演出 */}
        {user.role === Role.Merchant
          ? (
            <Tab.Screen name={TabNav.Publish}>
              {(props) => <UpsertShow {...props} user={user}></UpsertShow>}
            </Tab.Screen>
          )
          : null
        }
        {/* 我的 */}
        <Tab.Screen name={TabNav.Mine}>
          {(props) => <Mine {...props} user={user} userDispatch={userDispatch}></Mine>}
        </Tab.Screen>
      </Tab.Navigator>
    </NavigationContainer>
  );
};

export default LoginHOC(Route);
