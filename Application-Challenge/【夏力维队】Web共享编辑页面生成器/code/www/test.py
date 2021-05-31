doms=[["a",1,[]],["b",1,[["c",1,[["d",1,[]],["e",1,[["f",1,[]]]]]],["d",1,[]]]]]
dom_tree=["b","c","e"]
dom_owner = "e"
dom_aim = None

_doms = doms
for dom in dom_tree:
    _doms = _doms
    for _dom in _doms:
        if dom == _dom[0]:
            if dom_owner == _dom[0]:
                dom_aim = _dom
                dom_aim[1]=2
                break
            _doms = _dom[2]
            break

# def update_dom(doms,dom_tree,dom_owner,x,y)