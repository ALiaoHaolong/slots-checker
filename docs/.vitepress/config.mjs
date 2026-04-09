import { defineConfig } from 'vitepress'
import { withSidebar } from 'vitepress-sidebar'
import { withI18n } from 'vitepress-i18n'

// VitePress 基础配置 https://vitepress.dev/zh/
const vitePressOptions = {
  // Site Metadata
  title: 'Slots Checker',
  titleTemplate: true,
  description: 'This mod provides a command to view and modify any player\'s inventory, hotbar, ender chest, armor, and offhand slots (requires level 4 admin privileges).',
  head: [
      [ 'link', { rel: 'icon', href: '/slots-checker/icon.png' } ],
  ],
  lang: 'en-US',
  base: '/slots-checker/',
  // Routing
  cleanUrls: false,
  rewrites: {
    'en/:rest*': ':rest*'
  },
  // Build
  // Theming
  themeConfig: {
    search: {
      provider: 'local',
    },
    logo: '/icon.png',
    socialLinks: [
      { icon: 'modrinth', link: 'https://modrinth.com/mod/slots-checker' },
      { icon: 'curseforge', link: 'https://www.curseforge.com/minecraft/mc-mods/slots-checker' },
      // { icon: {}, link: 'https://www.mcmod.cn/class/8936.html' },
      { icon: 'github', link: 'https://github.com/aliaohaolong/slots-checker' },
    ],
  },
  appearance: true,
  lastUpdated: true,                  // 显示最后更新时间（基于 /git log 的时间）
  // Customization
  // Build Hooks
}

// 国际化配置 https://vitepress-i18n.cdget.com/
const vitePressI18nOptions = {
  locales: [
    { path: 'en', locale: 'en' },     // 英文，路径隐藏
    { path: 'zh', locale: 'zhHans' }  // 简体中文
  ],
  rootLocale: 'en',                   // 默认语言为英文
  searchProvider: 'local',
  searchOptions: undefined,           // Default
  disableAutoSetLangValue: false,     // Default
  debugPrint: false,                  // Default
  label: undefined,                   // Default
  link: undefined,                    // Default
  lang: undefined,                    // Default
  title: undefined,                   // Default
  titleTemplate: 'Slots Checker',     // Default
  description: {
    en: 'This mod provides a command to view and modify any player\'s inventory, hotbar, ender chest, armor, and offhand slots (requires level 4 admin privileges).',
    zh: '这个模组提供了一个命令，可查看和修改任何玩家的背包、快捷栏、末影箱、护甲以及副手格子（需要 4 级权限等级）。',
  },
  head: undefined,                    // Default
  themeConfig: {
    en: {
      nav: [
        { text: 'Introduction', link: '/introduction/slots-checker' },
        { text: 'Downloads', link: 'https://modrinth.com/mod/slots-checker/versions' },
        { text: 'Changelogs', link: '/changelogs/1.0.0', activeMatch: '/changelogs/' },
      ],
      footer: {
        message: 'Released under the Apache License, Version 2.0.',
        copyright: 'Copyright © 2026-present Haolong Liao',
      },
      editLink: {
        pattern: 'https://github.com/ALiaoHaolong/slots-checker/tree/master/docs/:path',
      },
    },
    zh: {
      nav: [
        { text: '简介', link: '/zh/introduction/slots-checker' },
        { text: '下载', link: 'https://modrinth.com/mod/slots-checker/versions' },
        { text: '更新日志', link: '/zh/changelogs/1.0.0', activeMatch: '/changelogs/' },
      ],
      footer: {
        message: '基于 Apache License, Version 2.0 协议发布。',
        copyright: '版权所有 © 2026-至今 廖浩龙',
      },
      editLink: {
        pattern: 'https://github.com/ALiaoHaolong/slots-checker/tree/master/docs/:path',
      },
    },
  },
}

// 自动生成侧边栏基础配置 https://vitepress-sidebar.cdget.com/zhHans/
const vitePressSidebarCommonOptions = {
  // ============ [ RESOLVING PATHS ] ============
  // documentRootPath: '/',
  // scanStartPath: null,
  // resolvePath: null,
  // basePath: null,
  // followSymlinks: false,
  //
  // ============ [ GROUPING ] ============
  // collapsed: true,
  // collapseDepth: 2,
  // rootGroupText: 'Contents',
  // rootGroupLink: 'https://github.com/jooy2',
  // rootGroupCollapsed: false,
  //
  // ============ [ GETTING MENU TITLE ] ============
  // useTitleFromFileHeading: true,
  useTitleFromFrontmatter: true,
  // useFolderLinkFromIndexFile: false,
  useFolderTitleFromIndexFile: true,
  // frontmatterTitleFieldName: 'title',
  //
  // ============ [ GETTING MENU LINK ] ============
  // useFolderLinkFromSameNameSubFile: false,
  // useFolderLinkFromIndexFile: false,
  // folderLinkNotIncludesFileName: false,
  //
  // ============ [ INCLUDE / EXCLUDE ] ============
  // excludeByGlobPattern: ['README.md', 'folder/'],
  // excludeFilesByFrontmatterFieldName: 'exclude',
  // excludeByFolderDepth: undefined,
  // includeDotFiles: false,
  // includeEmptyFolder: false,
  // includeRootIndexFile: false,
  // includeFolderIndexFile: false,
  //
  // ============ [ STYLING MENU TITLE ] ============
  // hyphenToSpace: true,
  // underscoreToSpace: true,
  // capitalizeFirst: false,
  // capitalizeEachWords: false,
  // keepMarkdownSyntaxFromTitle: false,
  // removePrefixAfterOrdering: false,
  // prefixSeparator: '.',
  //
  // ============ [ SORTING ] ============
  // manualSortFileNameByPriority: ['first.md', 'second', 'third.md'],
  // sortFolderTo: null,
  // sortMenusByName: false,
  // sortMenusByFileDatePrefix: false,
  sortMenusByFrontmatterOrder: true,
  // frontmatterOrderDefaultValue: 0,
  // sortMenusByFrontmatterDate: false,
  // sortMenusOrderByDescending: false,
  // sortMenusOrderNumericallyFromTitle: false,
  // sortMenusOrderNumericallyFromLink: false,
  //
  // ============ [ MISC ] ============
  // debugPrint: false,
}

// 多侧边栏配置
const vitePressSidebarOptions = [
  {
    ...vitePressSidebarCommonOptions,
    documentRootPath: `/docs/en`,
    resolvePath: '/',
  },
  {
    ...vitePressSidebarCommonOptions,
    basePath: '/zh/',
    documentRootPath: `/docs/zh`,
    resolvePath: '/zh/',
  },
]

export default defineConfig(
    withSidebar(withI18n(vitePressOptions, vitePressI18nOptions), vitePressSidebarOptions)
)
