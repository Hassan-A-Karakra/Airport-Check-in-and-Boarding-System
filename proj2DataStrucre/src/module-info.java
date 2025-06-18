module proj2DataStrucre {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
	requires javafx.base;

    exports application; // تصدير الحزمة ليتمكن JavaFX من الوصول إليها
    opens application to javafx.base, javafx.graphics; // فتح الحزمة للوصول باستخدام الانعكاس (Reflection)
}
