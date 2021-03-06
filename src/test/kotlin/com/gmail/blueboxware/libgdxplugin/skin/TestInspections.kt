package com.gmail.blueboxware.libgdxplugin.skin

import com.gmail.blueboxware.libgdxplugin.LibGDXCodeInsightFixtureTestCase
import com.gmail.blueboxware.libgdxplugin.filetypes.skin.inspections.*
import com.intellij.codeInspection.LocalInspectionTool

/*
 * Copyright 2017 Blue Box Ware
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class TestInspections : LibGDXCodeInsightFixtureTestCase() {

  fun testNonExistingClassInspection() {
    doTest(SkinNonExistingClassInspection())
  }

  fun testNonExistingFieldInspection() {
    doTest(SkinNonExistingFieldInspection())
  }

  fun testMalformedColorStringInspection() {
    doTest(SkinMalformedColorStringInspection())
  }

  fun testNonExistingResourceAliasInspection() {
    doTest(SkinNonExistingResourceAliasInspection())
  }

  fun testTypeInspection1() {
    doTest(SkinTypeInspection())
  }

  fun testTypeInspection2() {
    doTest(SkinTypeInspection())
  }

  fun testTypeInspection3() {
    doTest(SkinTypeInspection())
  }

  fun testDuplicateResourceNameInspection() {
    doTest(SkinDuplicateResourceNameInspection())
  }

  fun testMissingPropertyInspection() {
    doTest(SkinMissingPropertyInspection())
  }

  fun testDuplicatePropertyInspection() {
    doTest(SkinDuplicatePropertyInspection())
  }

  fun testInspectionNameInspection() {
    doTest(SkinInspectionNameInspection())
  }

  fun testSuppression() {
    myFixture.enableInspections(SkinMalformedColorStringInspection(), SkinDuplicatePropertyInspection(), SkinNonExistingFieldInspection())
    myFixture.testHighlighting(true, false, false, "suppression.skin")
  }

  fun testNonExistingFontFileInspection() {
    myFixture.copyDirectoryToProject("x", "x")
    myFixture.copyDirectoryToProject("z", "z")
    doTest(SkinNonExistingFontFileInspection())
  }

  private fun doTest(inspection: LocalInspectionTool) {
    myFixture.enableInspections(inspection)
    myFixture.testHighlighting(true, false, false, getTestName(true) + ".skin")
  }

  override fun setUp() {
    super.setUp()

    addLibGDX()
    addKotlin()

    myFixture.copyFileToProject("com/example/ColorArrayHolder.java")
    myFixture.copyFileToProject("com/example/KColorArrayHolder.kt")

  }

  override fun getBasePath() = "/filetypes/skin/inspections/"
}