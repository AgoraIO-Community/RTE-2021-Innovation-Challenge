import Api from 'constants/api'
import request from 'services/request'
import { HTTPResponse } from 'types/http'
import { AxiosResponse } from 'axios'
import { Tip } from 'types/search'

export interface SearchResponse extends HTTPResponse {
  data: Tip
}

type Search = (searchText: string) => Promise<AxiosResponse<SearchResponse>>

const search: Search = async searchText => {
  return await request.get<SearchResponse>(
    Api.Entry.Search,
    {
      params: { searchText }
    }
  )
}

export default search
