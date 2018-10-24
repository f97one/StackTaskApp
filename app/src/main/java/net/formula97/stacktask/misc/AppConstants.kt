package net.formula97.stacktask.misc

class AppConstants {

    companion object {
        /**
         * 優先度：デフォルト
         */
        const val PRIORITY_DEFAULT: Int = 1
        /**
         * 優先度：中
         */
        const val PRIORITY_MEDIUM: Int = 2
        /**
         * 優先度：高
         */
        const val PRIORITY_HIGH: Int = 3
        /**
         * 内部処理で使う時刻フォーマット
         */
        const val APP_STANDARD_DATETIME_FORMAT: String = "yyyy/MM/dd HH:mm"

        /**
         * 期日順
         */
        const val ORDER_BY_DUE_DATE: Int = 10
        /**
         * Spinner位置：期日順
         */
        const val POSITION_DUE_DATE: Int = 0
        /**
         * 優先度順
         */
        const val ORDER_BY_PRIORITY: Int = 11
        /**
         * Spinner位置：優先度順
         */
        const val POSITION_PRIORITY:Int = 1
        /**
         * 名前順
         */
        const val ORDER_BY_NAME: Int = 12
        /**
         * Spinner位置：名前順
         */
        const val POSITION_NAME: Int = 2
        /**
         * 処理タイプ：追加
         */
        const val SUBMIT_ADD = "add"
        /**
         * 処理タイプ：更新
         */
        const val SUBMIT_UPDATE = "update"
    }
}