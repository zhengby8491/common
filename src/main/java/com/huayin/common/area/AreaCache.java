package com.huayin.common.area;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.huayin.common.area.vo.Area;

/**
 * <pre>
 * 省市区缓存类
 * </pre>
 * @author chenjian
 * @version 1.0, 2010-9-25
 */
public class AreaCache
{
	// 存放所有区域对象
	private Map<Long, Area> addressMap = new HashMap<Long, Area>();

	// 存放省份对象
	private Map<Long, Area> provinceMap = new HashMap<Long, Area>();

	private static AreaCache address = null;

	Logger log = Logger.getLogger(this.getClass());

	// 存在在当前类路径中
	private static String path = "address.xml";

	private AreaCache()
	{
		loadAll();
	}

	public static AreaCache getInstance()
	{
		if (address == null)
		{
			synchronized (AreaCache.class)
			{
				if (address == null)
				{
					address = new AreaCache();
				}
			}
		}
		return address;
	}

	public Area get(Long id)
	{
		return clone(addressMap.get(id));
	}

	public List<Area> getAllProvince()
	{
		return cloneList(new ArrayList<Area>(provinceMap.values()));
	}

	private void loadAll()
	{
		log.info("开始加载区域xml配置文件{" + path + "}");
		InputStream in = null;
		try
		{
			in = this.getClass().getResourceAsStream(path);
		}
		catch (Exception e1)
		{
			throw new RuntimeException("配置文件不存在，路径:" + path);
		}
		SAXReader reader = new SAXReader();
		Document document = null;
		try
		{
			document = reader.read(in);
		}
		catch (DocumentException e)
		{
			log.error("加载区域xml配置文件{" + path + "}失败");
			throw new RuntimeException("加载配置文件错误:" + path);
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException ex)
				{
					throw new RuntimeException("加载配置文件(" + path + ")异常");
				}
			}
		}
		Element element = document.getRootElement();
		iterator(element, null);
	}

	@SuppressWarnings("unchecked")
	private void iterator(Element element, Area vo)
	{
		// 获取该节点下所有子节点
		if (element == null)
			return;
		List<Element> elements = (List<Element>) element.elements();
		if (elements == null || elements.size() <= 0)
			return;
		for (Element e : elements)
		{
			Area ads = new Area();
			ads.setId(Long.valueOf(e.attributeValue("id")));
			String name = e.attributeValue("name");
			if (name == null && (e.elements() == null || e.elements().size() <= 0))
			{
				ads.setName(e.getTextTrim());
			}
			else
			{
				ads.setName(name);
			}
			ads.setLevel(Long.valueOf(e.attributeValue("level")));
			if (vo != null)
			{
				List<Area> childs = vo.getChildList();
				if (childs == null)
				{
					childs = new ArrayList<Area>();
					vo.setChildList(childs);
				}
				childs.add(ads);
			}
			else
			{
				provinceMap.put(ads.getId(), ads);
			}
			addressMap.put(ads.getId(), ads);
			iterator(e, ads);
		}
	}

	private Area clone(Area vo)
	{
		Area ads = null;
		if (vo != null)
		{
			ads = new Area();
			ads.setId(vo.getId());
			ads.setName(vo.getName());
			ads.setLevel(vo.getLevel());
			List<Area> voList = vo.getChildList();
			if (voList != null && voList.size() > 0)
			{
				List<Area> adsList = new ArrayList<Area>();
				for (int i = 0; i < voList.size(); i++)
				{
					Area v = voList.get(i);
					Area temp = new Area();
					temp.setId(v.getId());
					temp.setName(v.getName());
					temp.setLevel(v.getLevel());
					adsList.add(temp);
				}
				ads.setChildList(adsList);
			}
		}
		return ads;
	}

	private List<Area> cloneList(List<Area> list)
	{
		List<Area> areaList = new ArrayList<Area>();
		if (list != null)
		{
			for (Area area : list)
			{
				Area temp = new Area();
				temp.setId(area.getId());
				temp.setName(area.getName());
				temp.setLevel(area.getLevel());
				areaList.add(temp);
			}
		}
		return areaList;
	}
}
