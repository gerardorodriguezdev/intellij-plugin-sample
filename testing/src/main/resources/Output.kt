
@file:JsModule("inputFile")
@file:Suppress(
    "NON_EXTERNAL_DECLARATION_IN_INAPPROPRIATE_FILE",
)

package .



/**
 * @license
 * Copyright 2016 Google Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
/* import { MDCComponent } from '@material/base/component'; */

/* import { SpecificEventListener } from '@material/base/types'; */

/* import { FocusTrap } from '@material/dom/focus-trap'; */

/* import { MDCList, MDCListFactory } from '@material/list/component'; */

/* import { MDCDrawerAdapter } from './adapter'; */

/* import { MDCDismissibleDrawerFoundation } from './dismissible/foundation'; */

/* import { MDCModalDrawerFoundation } from './modal/foundation'; */

/* import * as util from './util'; */

/* import { MDCDrawerFocusTrapFactory } from './util'; */

external val /* { cssClasses, strings } */: Any? /* should be inferred */

/**
 * @events `MDCDrawer:closed {}` Emits when the navigation drawer has closed.
 * @events `MDCDrawer:opened {}` Emits when the navigation drawer has opened.
 */

external class MDCDrawer : MDCComponent<MDCDismissibleDrawerFoundation> {
/**
     * @return boolean Proxies to the foundation's `open`/`close` methods.
     * Also returns true if drawer is in the open position.
     */

var open: Boolean
            
// initialSyncWithDOM()

val list: MDCList?
            
fun initialize(focusTrapFactory: MDCDrawerFocusTrapFactory, listFactory: MDCListFactory)
fun initialSyncWithDOM()
fun destroy()
fun getDefaultFoundation()
        
companion object {
fun attachTo(root: HTMLElement): MDCDrawer
}
        
}
    
    