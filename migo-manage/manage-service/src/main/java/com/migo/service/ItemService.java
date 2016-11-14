package com.migo.service;



import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.migo.mapper.ItemMapper;
import com.migo.pojo.EasyUIDataGridResult;
import com.migo.pojo.Item;
import com.migo.pojo.ItemDesc;
import com.migo.utils.IDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Author  知秋
 * Created by kauw on 2016/11/12.
 */
@Service
public class ItemService extends BaseService<Item> {
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemDescService itemDescService;
    //desc参考前端页面传过来的数据是序列化成字符串的
    public Boolean saveItem(Item item,String desc){
        // 1、生成商品id
        long itemId = IDUtils.genItemId();
        // 2、对TbItem对象补全属性。
        item.setId(itemId);

        //'商品状态，1-正常，2-下架，3-删除'
        item.setStatus(1);

        //保存商品数据
        Integer save = super.save(item);

        ItemDesc itemDesc=new ItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(item.getId());

        //保存商品描述数据
        Integer save1 = this.itemDescService.save(itemDesc);
        return save.intValue()==1&&save1.intValue()==1;

    }

    /**
     * 查询商品列表
     */
    public EasyUIDataGridResult getItemList(Integer page,Integer rows){

        //执行查询
        Example example=new Example(Item.class);
        example.orderBy("updated").desc();

       // example.setOrderByClause("updated DESC");
        PageHelper.startPage(page,rows);

        List<Item> itemList = this.itemMapper.selectByExample(example);

        PageInfo<Item> pageInfo=new PageInfo<>(itemList);
        //返回处理结果
        EasyUIDataGridResult result=new EasyUIDataGridResult();
        result.setTotal(pageInfo.getTotal());
        result.setRows(pageInfo.getList());
        //return new EasyUIDataGridResult(pageInfo.getList(), pageInfo.getTotal());
        return result;
    }

    /**
     * 商品修改
     */
    
}