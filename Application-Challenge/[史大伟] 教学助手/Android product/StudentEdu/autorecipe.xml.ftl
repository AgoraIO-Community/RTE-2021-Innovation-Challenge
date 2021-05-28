<?xml version="1.0"?>
<recipe>

    <mergexml from="AndroidManifest.xml.sdw" to="AndroidManifest.xml" />
    <instantiate from="Activity.kt.page.sdw"
      to="activity/{{activityClass}}.kt" />
    <instantiate from="Fragment.kt.page.sdw"
      to="fragment/{{activityClass}}.kt" />
    <instantiate from="View.kt.page.sdw"
      to="view/{{viewClass}}.kt" />
    <instantiate from="Presenter.kt.page.sdw"
      to="presenter/{{presenterClass}}.kt" />
    <instantiate from="Adapter.kt.page.sdw"
      to="adapter/{{adapterClass}}.kt" />
    <instantiate from="ViewModel.kt.page.sdw"
      to="viewmodel/{{viewModelClass}}.kt" />

	<instantiateres from="activity_layout.xml.page.sdw"
                 to="layout/{{layoutName}}.xml" />
	<instantiateres from="fragment_layout.xml.page.sdw"
                 to="layout/{{layoutName}}.xml" />
	<instantiateres from="adapter_item_layout.xml.page.sdw"
                 to="layout/{{itemlayoutName}}.xml" />

</recipe>
