/*
 * CallVault: FOSS call recording, self-contained over embedded ADB
 *  Copyright (C) 2026-present The CallVault Authors
 *  This software is licensed under the GNU General Public License v3 or later, with additional terms as permitted under Section 7.
 *  The full license text is available in the LICENSE file at the root of this project.
 *  This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package com.baba.callvault.integrations.adb

import android.content.Context
import android.provider.Settings

object DeveloperOptions {

    fun isEnabled(context: Context): Boolean = state(context) == DeveloperOptionsState.ON

    fun state(context: Context): DeveloperOptionsState = DeveloperOptionsPolicy.of(
        settingSaysOn = runCatching {
            Settings.Global.getInt(context.contentResolver, Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1
        }.getOrDefault(false),
        usbDebugging = AdbShell.usbDebuggingState(context),
        wirelessDebuggingOn = runCatching { AdbShell.isWirelessDebuggingEnabled(context) }.getOrDefault(false),
        adbd = runCatching { AdbShell.adbdState() }.getOrDefault(AdbdState.UNKNOWN),
    )

    fun isExplicitlyDisabled(context: Context): Boolean = state(context) == DeveloperOptionsState.OFF
}
