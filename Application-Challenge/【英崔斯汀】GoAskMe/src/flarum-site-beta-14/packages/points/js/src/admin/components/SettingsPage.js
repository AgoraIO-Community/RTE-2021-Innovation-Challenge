import Button from 'flarum/components/Button';
import LoadingIndicator from 'flarum/components/LoadingIndicator';
import Placeholder from 'flarum/components/Placeholder';
import Page from 'flarum/components/Page';

import SettingsPageItem from './SettingsPageItem';

export default class SettingsPage extends Page {

    init() {
        super.init();

        this.loading = true;

        this.page = 0;
        this.pageSize = 20;
    }

    config(isInitialized) {
        super.config(...arguments);

        if (isInitialized) return;

        this.refresh();
    }

    view() {
        let next, prev;

        if (this.nextResults === true) {
            next = Button.component({
                className: 'Button Button--PageList-next',
                icon: 'fas fa-angle-right',
                onclick: this.loadNext.bind(this),
            });
        }

        if (this.prevResults === true) {
            prev = Button.component({
                className: 'Button Button--PageList-prev',
                icon: 'fas fa-angle-left',
                onclick: this.loadPrev.bind(this),
            });
        }

        return (
            <div className="ExtensionsPage">
                <div className="ExtensionsPage-header">
                    <div className="container">
                        头标题描述部分
                    </div>
                </div>
                <br />
                <div className="BannedIpsPage-table">
                    <div className="container">
                        {this.loading ? (
                            LoadingIndicator.component()
                        ) : app.store.all('points/list')[0].data.list.length ? (
                            <table style={{ width: '100%', textAlign: 'center' }} className="PermissionGrid PointsList">
                                <thead>
                                    <tr className="PermissionGrid-section">
                                        <th>用户名</th>
                                        <th>类型</th>
                                        <th>扣除Points</th>
                                        <th>剩余Points</th>
                                        <th>钱包地址</th>
                                        <th>提交时间</th>
                                        <th>操作</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {app.store.all('points/list')[0].data.list
                                        .map((bannedIP) => SettingsPageItem.component({ bannedIP }))}
                                </tbody>
                            </table>
                        ) : (
                                    <div>{Placeholder.component({ text: app.translator.trans('goaskme-points.admin.empty_text') })}</div>
                                )}
                    </div>
                </div>
                <div>
                    {prev}
                    {next}
                </div>
            </div>
        );
    }


    refresh() {
        if(app.store.all('points/list').length){
            // 清空数据
            app.store.all('points/list')[0].data.list=[]
        }
        return this.loadResults().then(this.parseResults.bind(this));
    }

    /**
     * Load a new page of Pages results.
     *
     * @param {Integer} page number.
     * @return {Promise}
     */
    loadResults() {
        this.loading = true;
        const offset = this.page * this.pageSize;

        return app.store.find('points/list', { offset, limit: this.pageSize })
    }

    /**
     * Load the next page of results.
     *
     * @public
     */
    loadNext() {
        if (this.nextResults === true) {
            this.page++;
            this.refresh();
        }
    }

    /**
     * Load the previous page of results.
     *
     * @public
     */
    loadPrev() {
        if (this.prevResults === true) {
            this.page--;
            this.refresh();
        }
    }

    /**
     * Parse results and append them to the page list.
     *
     * @param {Page[]} results
     * @return {Page[]}
     */
    parseResults(results) {
        this.loading = false;
        this.nextResults = !!results.data.next;
        this.prevResults = !!results.data.prev;
        m.lazyRedraw();
    }
}
